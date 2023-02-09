package com.group04.tgdd.service.payment.paymentFactory;

import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.exception.OutOfStockException;
import com.group04.tgdd.exception.PaymentException;
import com.group04.tgdd.model.OrderItem;
import com.group04.tgdd.model.Orders;
import com.group04.tgdd.model.ProductColor;
import com.group04.tgdd.model.Users;
import com.group04.tgdd.repository.*;
import com.group04.tgdd.service.OrderService;
import com.group04.tgdd.service.PaypalService;
import com.group04.tgdd.service.UserService;
import com.group04.tgdd.service.email.EmailSenderService;
import com.group04.tgdd.service.payment.async.PaymentAsync;
import com.group04.tgdd.utils.VNpayUtils;
import com.group04.tgdd.utils.constant.StateOrderConstant;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class VNpayPayment extends PaymentProcess {

    public VNpayPayment(OrderRepo orderRepo, UsersRepo usersRepo, ProductColorRepo productColorRepo, PaymentRepo paymentRepo, UserService userService, PaypalService paypalService, ModelMapper modelMapper, PaymentAsync paymentAsync, EmailSenderService emailSenderService, OrderItemRepo orderItemRepo) {
        super(orderRepo, usersRepo, productColorRepo, paymentRepo, userService, paypalService, modelMapper, paymentAsync, emailSenderService, orderItemRepo);
    }

    @Override
    public Object createOrder(HttpServletRequest request, Orders orders) throws UnsupportedEncodingException {
        orders.setState(StateOrderConstant.UnPaid);
        orderRepo.save(orders);
        CompletableFuture<Boolean> check = paymentAsync.asyncCheckAndUpdateQuantityProduct(orders.getOrderItems());

        Map vnp_Params = vnpayParams(orders,request);
        //Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNpayUtils.hmacSHA512(VNpayUtils.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNpayUtils.vnp_PayUrl + "?" + queryUrl;
        check.exceptionally(ex -> {
            throw new PaymentException(409,"Out of stock");
        });

        return paymentUrl;
    }

    @Transactional
    @Override
    public Object confirmPaymentAndSendMail(Long orderId, String... param) throws MessagingException, TemplateException, IOException {
        Orders orders = orderRepo.findById(orderId).orElseThrow(()->{
           throw new NotFoundException("Order ID not found");
        });
        orders.setState(StateOrderConstant.Paid);
        orderRepo.save(orders);
        sendEmailOrder(orders);
        return orders.getId();
    }
    @Transactional
    @Override
    public void cancelOrder(Object orderId) {
        Orders orders = orderRepo.findById((Long) orderId).orElseThrow(()->{
            throw new NotFoundException("Order ID not found");
        });
        orders.setState(StateOrderConstant.Cancel);
        returnProduct(orders.getOrderItems());
    }

    private Map vnpayParams(Orders orders,HttpServletRequest request){
        BigDecimal amount = orders.getOrderdetail().getTotalPrice();
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = VNpayUtils.getIpAddress(request);

        Map vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNpayUtils.vnp_Version);
        vnp_Params.put("vnp_Command", VNpayUtils.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNpayUtils.vnp_TmnCode);

        vnp_Params.put("vnp_CurrCode", VNpayUtils.vnp_currCode);
        vnp_Params.put("vnp_OrderInfo", VNpayUtils.vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", VNpayUtils.orderType);
        vnp_Params.put("vnp_ReturnUrl", VNpayUtils.vnp_Returnurl + "/" + orders.getId());

        String bank_code = "NCB";
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_Amount", String.valueOf(amount.multiply(BigDecimal.valueOf(100))));

        String locate = request.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }

        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        String vnp_CreateDate = df.format(date);

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        date.setTime(date.getTime()+ 5*60*1000);
        String vnp_ExpireDate = df.format(date);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        Users userOrder =  orders.getOrderUser();
        vnp_Params.put("vnp_Bill_Mobile",userOrder.getPhone());
        vnp_Params.put("vnp_Bill_Email", userOrder.getEmail());

        String fullName = userOrder.getName();
        if (fullName != null && !fullName.isEmpty()) {
            int idx = fullName.indexOf(' ');
            if (idx!=-1) {
                String firstName = fullName.substring(0, idx);
                String lastName = fullName.substring(fullName.lastIndexOf(' ') + 1);
                vnp_Params.put("vnp_Bill_FirstName", firstName);
                vnp_Params.put("vnp_Bill_LastName", lastName);
            }else {
                vnp_Params.put("vnp_Bill_FirstName", fullName);
                vnp_Params.put("vnp_Bill_LastName", fullName);
            }
        }
        return vnp_Params;
    }
}

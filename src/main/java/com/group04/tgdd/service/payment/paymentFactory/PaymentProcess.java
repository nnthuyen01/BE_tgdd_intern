package com.group04.tgdd.service.payment.paymentFactory;

import com.group04.tgdd.dto.OrderReq;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.exception.PaymentException;
import com.group04.tgdd.model.*;
import com.group04.tgdd.repository.*;
import com.group04.tgdd.service.PaypalService;
import com.group04.tgdd.service.UserService;
import com.group04.tgdd.service.email.EmaiType;
import com.group04.tgdd.service.email.EmailSenderService;
import com.group04.tgdd.service.payment.async.PaymentAsync;
import com.group04.tgdd.utils.MoneyConvert;
import com.group04.tgdd.utils.Utils;
import com.group04.tgdd.utils.constant.PaymentConstant;
import com.group04.tgdd.utils.constant.StateOrderConstant;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Slf4j
public abstract class PaymentProcess {
    protected final OrderRepo orderRepo;
    protected final UsersRepo usersRepo;
    protected final ProductColorRepo productColorRepo;
    protected final PaymentRepo paymentRepo;
    protected final UserService userService;
    protected final PaypalService paypalService;
    protected final ModelMapper modelMapper;
    protected final PaymentAsync paymentAsync;
    protected final EmailSenderService emailSenderService;
    protected final OrderItemRepo orderItemRepo;

    protected static final String URL_PAYPAL_SUCCESS = "pay/success";
    protected static final String URL_PAYPAL_CANCEL = "pay/cancel";

    final String sold_out_product = "Sold out Product";
    final int ESTIMATE_DELIVERY_DATE = 3;

    @Transactional
    public Object initOrder(OrderReq orderReq,HttpServletRequest request){
        Long userId = Utils.getIdCurrentUser();

        Users users = usersRepo.getReferenceById(userId);
        Payment payment = paymentRepo.findAllByName(orderReq.getPaymentMethod());
        if (payment==null){
            payment = new Payment();
            payment.setName(orderReq.getPaymentMethod());
            paymentRepo.save(payment);
        }
        LocalDateTime date = LocalDateTime.now();
        Orders orders = new Orders();
        orders.setState(StateOrderConstant.Pending);
        orders.setCreateTime(date);
        orders.setOrderUser(users);

        OrderDetail orderDetail = modelMapper.map(orderReq,OrderDetail.class);

        Map<String, Object> result = getTotalPriceAndQuantityAndOrderItems(orderReq,orders);

        BigDecimal total= (BigDecimal) result.get("total");
        int quantity = (int) result.get("quantity");
        List<OrderItem> orderItems = (List<OrderItem>) result.get("orderItems");

        orderDetail.setTotalPrice(total);
        orderDetail.setQuantity(quantity);
        orderDetail.setPayment(payment);
        orderDetail.setOrders(orders);

        orders.setOrderdetail(orderDetail);
        orders.setOrderItems(orderItems);

        return orders;
    }
    @Transactional
    Map<String, Object> getTotalPriceAndQuantityAndOrderItems(OrderReq orderReq, Orders orders){
        AtomicReference<BigDecimal> total= new AtomicReference<>(BigDecimal.ZERO);
        AtomicInteger quantity = new AtomicInteger();
        List<OrderItem> orderItems = new ArrayList<>();
        List<Long> listIdPrColor = new ArrayList<>();
        orderReq.getItems().forEach(item ->{
            listIdPrColor.add(item.getProductColorId());
        } );
        List<ProductColor> productColors = productColorRepo.findAllByIdIn(listIdPrColor);
        orderReq.getItems().forEach((item) -> {
            ProductColor productColor = productColors.stream()
                    .filter(prColor ->
                            prColor.getId().equals(item.getProductColorId())).findFirst().orElseThrow(
                            ()-> new NotFoundException("Product not found")
                    );
            int currentQuantity = productColor.getQuantity();
            if (currentQuantity < item.getQuantity()) {
                throw new PaymentException(400, sold_out_product);
            }
            BigDecimal price = productColor.getProductOption().getPrice();
            BigDecimal marketPrice = MoneyConvert.calculaterPrice(price,productColor.getProductOption().getPromotion());
            String itemName = productColor.getProductOption().getProduct().getName()+ " "+
            productColor.getProductOption().getOptionName()+ " " + productColor.getColor().getName();
            orderItems.add(new OrderItem(
                    null,
                    itemName,
                    item.getQuantity(),
                    marketPrice,
                    orders,
                    productColor
            ));
            quantity.addAndGet(item.getQuantity());
            BigDecimal currentPrice = marketPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            total.set(total.get().add(currentPrice));

        });
        Map<String, Object> result = new HashMap<>();
        result.put("total",total.get());
        result.put("quantity",quantity.get());
        result.put("orderItems",orderItems);
        return result;
    }

    @Transactional
    public void returnProduct(List<OrderItem> orderItems){
        List<ProductColor> productColors = orderItemRepo.getAllProductInOrderItems(orderItems);

        for (ProductColor productColor : productColors){
            OrderItem orderItem = orderItems.stream().filter(item ->
                 item.getProductColor().getId().equals(productColor.getId())
             ).findFirst().orElseThrow(()-> new NotFoundException("Order item not found"));
            productColor.increaseQuantityProduct(orderItem.getQuantity());
        }
        productColorRepo.saveAll(productColors);
    }

    @SneakyThrows
    protected void sendEmailOrder(Orders orders){
        Map<String,Object> model = new HashMap<>();
        Locale locale = new Locale("vn", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        model.put("orderId",orders.getId());
        model.put("total",currencyFormatter.format(orders.getOrderdetail().getTotalPrice()));
        model.put("deliveryAddress",orders.getOrderdetail().getDeliveryAddress());
        model.put("subject","Thank You For Your Order!");

        LocalDateTime date = orders.getCreateTime().plusDays(ESTIMATE_DELIVERY_DATE);
        String dateFormat= DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(date);
        model.put("deliveryDate",dateFormat);

        String title;
        if (orders.getOrderdetail().getPayment().getName().equals(PaymentConstant.COD)){
            if (orders.getState().equals(StateOrderConstant.Paid))
                title = "Your order has been confirmed and will be delivery within 3 days.";
            else title = "Your order has not been confirmed and is in the process of being confirmed by Admin.";
        }
        else title = String.format("Your order has been paid by %s and will be delivery within 3 days.",
                orders.getOrderdetail().getPayment().getName().toUpperCase());
        model.put("title",title);

        Map<String,String> items = new HashMap<>();
        orders.getOrderItems().stream().forEach(item ->{
            items.put(String.format("%s <b>(x%s)</br>",item.getItemName(),item.getQuantity()),currencyFormatter.format(item.getPrice()));
        });
        model.put("items",items);

        emailSenderService.sendEmail(orders.getOrderUser().getEmail(),model, EmaiType.ORDER);
    }

    public abstract Object createOrder(HttpServletRequest request, Orders orders) throws UnsupportedEncodingException;
    public abstract Object confirmPaymentAndSendMail(Long orderId, String... param) throws MessagingException, TemplateException, IOException;
    public abstract void cancelOrder(Object orderId);
}

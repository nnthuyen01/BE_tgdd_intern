package com.group04.tgdd.service.payment.async;

import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.exception.OutOfStockException;
import com.group04.tgdd.model.OrderItem;
import com.group04.tgdd.model.Orders;
import com.group04.tgdd.model.ProductColor;
import com.group04.tgdd.repository.OrderItemRepo;
import com.group04.tgdd.repository.OrderRepo;
import com.group04.tgdd.repository.ProductColorRepo;
import com.group04.tgdd.service.payment.paymentFactory.PaymentProcess;
import com.group04.tgdd.utils.constant.StateOrderConstant;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PaymentAsync {
    private final OrderItemRepo orderItemRepo;
    private final ProductColorRepo productColorRepo;
    private final int TIME_OUT = 5;


//    @Async
//    public void checkOrderPaypal(Long orderId) throws InterruptedException {
//        log.info(Thread.currentThread().getName()+ ": Paypal check Start");
//        TimeUnit.MINUTES.sleep(TIME_OUT);
//        Orders orders = orderRepo.findById(orderId).orElse(null);
//        if (!orders.getState().equals(StateOrderConstant.Paid)){
//            orders.setState(StateOrderConstant.Cancel);
//            orderRepo.save(orders);
//            productColorRepo.saveAll(PaymentProcess.returnProduct(orders));
//            log.info(Thread.currentThread().getName()+ ": Paypal check return Product and end Thread");
//            return;
//        }
//        log.info(Thread.currentThread().getName()+ ": Paypal check End");
//    }

    @Async
    public CompletableFuture<Boolean> asyncCheckAndUpdateQuantityProduct(List<OrderItem> orderItems) {
        synchronized (this) {
            log.info("async check and update quantity start");
            List<ProductColor> productColors = orderItemRepo.getAllProductInOrderItems(orderItems);
            for (OrderItem orderItem : orderItems) {
                ProductColor productColor = productColors.stream().filter(proColor ->
                                proColor.getId().equals(orderItem.getProductColor().getId())).findFirst()
                        .orElseThrow(() -> new NotFoundException("Product color not found"));

                if (orderItem.getQuantity() > productColor.getQuantity()) {
                    log.info("async check and update quantity throw exception");
                    throw new OutOfStockException("Out of stock");
                }
                productColor.increaseQuantityProduct(-orderItem.getQuantity());
            }

            productColorRepo.saveAllAndFlush(productColors);
            log.info("async check and update quantity end");
            return CompletableFuture.completedFuture(true);

        }

    }


}

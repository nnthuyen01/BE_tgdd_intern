package com.group04.tgdd.service;

import com.group04.tgdd.model.Payment;

import java.util.List;

public interface PaymentService {
    Payment findById(Long id);
    List<Payment> findAll();
    Payment save(Payment payment);
    Payment updatePayment(Payment payment);
    Boolean deletePayment(Long paymentId);
}

package com.group04.tgdd.service.iplm;

import com.group04.tgdd.model.Payment;
import com.group04.tgdd.repository.PaymentRepo;
import com.group04.tgdd.service.PaymentService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentIplm implements PaymentService {
    private final PaymentRepo paymentRepo;

    @Override
    public Payment findById(Long id) {
        Optional<Payment> payment = paymentRepo.findById(id);
        return payment.orElse(null);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepo.findAll();
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepo.save(payment);
    }

    @Override
    public Payment updatePayment(Payment payment) {
        Payment paymentUpdate = findById(payment.getId());
        if (paymentUpdate !=null) {
            paymentUpdate.setName(payment.getName());
            return paymentUpdate;
        }
        else return null;
    }

    @Override
    public Boolean deletePayment(Long paymentId) {
        boolean check = paymentRepo.existsById(paymentId);
        if (check){
            paymentRepo.deleteById(paymentId);
            return true;
        }else {
            return false;
        }
    }
}

package com.group04.tgdd.repository;

import com.group04.tgdd.model.Payment;
import com.twilio.twiml.voice.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment,Long> {
    Payment findAllByName(String name);

}

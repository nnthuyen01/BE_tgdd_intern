package com.group04.tgdd.service.twilio;

import com.group04.tgdd.dto.UserReq;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.type.PhoneNumber;
@Service
public class TwillioService {

    @Value("${twillio.client.id}")
    private static final String ACCOUNT_SID = "AC970caab4a1f7a9221fa227843c71edec";
    @Value("${twillio.client.secret}")
    private static final String AUTH_TOKEN = "263a14e809a45d156f6b56e74cdb7561";
    @Value("${twillio.client.from}")
    private String TWILLIO_SENDER = "(850) 809-7298";

    public void sendSMS(String phone, String token) {
        try {

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(new PhoneNumber(phone), new PhoneNumber(TWILLIO_SENDER),"Your OTP is: " + token)
                    .create();
            System.out.println(message.getSid());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

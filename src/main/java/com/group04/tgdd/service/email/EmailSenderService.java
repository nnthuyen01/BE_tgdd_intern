package com.group04.tgdd.service.email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;

@Service
@Async
@Slf4j
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Configuration configuration;

    final String REIGSTER_TEMPLATE = "register-template.ftl";
    final String ORDER_TEMPLATE = "order-template.ftl";
    final String FROM_EMAIL = "java.intern.group04@gmail.com";
    final String TYPE_EMAIL = "text/html";

    public void sendEmail(String toEmail,
                          Map<String,Object> model,
                          EmaiType type) throws MessagingException, IOException, TemplateException {
        log.info(Thread.currentThread().getName()+ "- send email start");
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage);
        Template template =null;
        if (type.equals(EmaiType.REGISTER)) {
            template = configuration.getTemplate(REIGSTER_TEMPLATE);
        }
        else if (type.equals(EmaiType.ORDER)){
            template = configuration.getTemplate(ORDER_TEMPLATE);
        }
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
        mimeMailMessage.setContent(html, TYPE_EMAIL);

        helper.setFrom(FROM_EMAIL);
        helper.setTo(toEmail);
        helper.setText(html,true);
        helper.setSubject((String) model.get("subject"));

        mailSender.send(mimeMailMessage);
        log.info(Thread.currentThread().getName()+ "- send email");
    }
}

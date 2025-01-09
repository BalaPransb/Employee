package com.app.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import reactor.core.publisher.Mono;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public Mono<Void> sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("your-email@gmail.com");

        mailSender.send(message);
        return Mono.empty();
    }

}
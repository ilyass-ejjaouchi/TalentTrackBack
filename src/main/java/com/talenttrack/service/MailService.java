package com.talenttrack.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String sujet, String body, String[] to) {
        for (String recipient : to) {
            sendSingleEmail(sujet, body, recipient);
        }
    }

    private void sendSingleEmail(String sujet, String body, String to) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("noreply.tennisClub@gmail.com");
        mail.setSubject(sujet);
        mail.setText(body);
        mail.setTo(to);
        javaMailSender.send(mail);
    }

}
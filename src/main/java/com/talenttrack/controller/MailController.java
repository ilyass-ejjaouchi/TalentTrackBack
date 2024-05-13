package com.talenttrack.controller;

import com.talenttrack.dto.request.EmailRequest;
import com.talenttrack.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/emails")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            mailService.sendEmail(emailRequest.getSubject(), emailRequest.getBody(), emailRequest.getTo());
            return "Email sent successfully!";
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}

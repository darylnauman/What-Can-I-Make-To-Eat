package com.ex.emailapi.controllers;

import com.ex.emailapi.services.EmailService;
import com.ex.emailapi.services.EmailServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/email")
public class Controllers {
    EmailService emailService = new EmailServiceImpl();
    @GetMapping(path = "sendEmail/{emailId}/{message}")
    public String sendEmail(@PathVariable("emailId") String emailId, @PathVariable String message) throws MessagingException, IOException {
        emailService.sendmail(emailId, message);
        return "Email sent successfully";
    }
}

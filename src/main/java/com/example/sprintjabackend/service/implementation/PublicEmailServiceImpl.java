package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.service.PublicEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class PublicEmailServiceImpl implements PublicEmailService {


    private final EmailService emailService;

    @Autowired
    public PublicEmailServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public boolean sendQuery(String email, String fullName, String phoneNumber, String message) throws MessagingException {
        this.emailService.sendingQuery(fullName,email,phoneNumber,message);
        return true;
    }
}

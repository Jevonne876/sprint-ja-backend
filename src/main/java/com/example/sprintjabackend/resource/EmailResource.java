package com.example.sprintjabackend.resource;

import com.example.sprintjabackend.service.PublicEmailService;
import com.example.sprintjabackend.service.implementation.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/")
public class EmailResource {


    private final PublicEmailService publicEmailService;

    @Autowired
    public EmailResource(PublicEmailService publicEmailService) {
        this.publicEmailService = publicEmailService;

    }


    @PostMapping("send-query/{fullName}/{email}/{phoneNumber}/{message}")

    public ResponseEntity<Boolean> sendQuery(@PathVariable("fullName") String fullName,
                                             @PathVariable("email") String email,
                                             @PathVariable("phoneNumber") String phoneNumber,
                                             @PathVariable("message") String message
                                             ) throws MessagingException {
        return new ResponseEntity<>(publicEmailService.sendQuery(fullName,email,phoneNumber,message),OK);
    }
}

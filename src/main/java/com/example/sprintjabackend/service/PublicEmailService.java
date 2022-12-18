package com.example.sprintjabackend.service;

import javax.mail.MessagingException;

public interface PublicEmailService {


    boolean sendQuery(String email, String fullName,String phoneNumber,String message) throws MessagingException;

}

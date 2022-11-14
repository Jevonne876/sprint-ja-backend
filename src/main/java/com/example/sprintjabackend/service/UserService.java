package com.example.sprintjabackend.service;

import com.example.sprintjabackend.exception.domain.EmailExistException;
import com.example.sprintjabackend.exception.domain.UserNotFoundException;
import com.example.sprintjabackend.exception.domain.UsernameExistException;
import com.example.sprintjabackend.model.User;

import java.util.Date;


public interface UserService {

    User register(Long trn, String firstName, String lastName, Date dateOfBirth,
                  String email, String password, String phoneNumber,
                  String address1, String address2, String pickUpBranch) throws UserNotFoundException, EmailExistException, UsernameExistException;


    User findUserByUsername(String username);

    User findUserByEmail(String email);
}

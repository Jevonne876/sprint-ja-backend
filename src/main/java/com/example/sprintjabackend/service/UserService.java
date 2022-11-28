package com.example.sprintjabackend.service;

import com.example.sprintjabackend.exception.domain.*;
import com.example.sprintjabackend.model.User;

import java.util.Date;


public interface UserService {

    User register(Long trn, String firstName, String lastName, Date dateOfBirth,
                  String email, String password, String phoneNumber,
                  String address1, String address2, String pickUpBranch) throws EmailExistException, TrnExistException, PhoneNumberException;

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserByTrn(Long trn);

    User findUserByPhoneNumber(String phoneNumber);
}

package com.example.sprintjabackend.service;

import com.example.sprintjabackend.exception.domain.EmailExistException;
import com.example.sprintjabackend.exception.domain.EmailNotFoundException;
import com.example.sprintjabackend.exception.domain.PhoneNumberException;
import com.example.sprintjabackend.exception.domain.TrnExistException;
import com.example.sprintjabackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    User register(Long trn, String firstName, String lastName, Date dateOfBirth,
                  String email, String password, String phoneNumber,
                  String address1, String address2, String pickUpBranch) throws EmailExistException, TrnExistException, PhoneNumberException, MessagingException;

    User updateUser(UUID userId, Long trn, String firstName, String lastName, Date dateOfBirth,
                    String email, String password, String phoneNumber,
                    String address1, String address2, String pickUpBranch) throws EmailExistException, TrnExistException, PhoneNumberException;

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserByTrn(Long trn);

    User findUserByPhoneNumber(String phoneNumber);

    User findUserByUserId(UUID userId);

    void resetPassword(String email) throws EmailNotFoundException, MessagingException;

    Page<User> findAllByRole(Pageable pageable);

    public Page<User> findAllAdminByRole(Pageable pageable);

    public void deleteUser(String username);



    User addNewUserFromAdmin(Long trn,
                             String firstName, String lastName,
                             Date dateOfBirth, String email, String phoneNumber,
                             String streetAddress, String parish,
                             String pickUpBranch) throws PhoneNumberException, EmailExistException, TrnExistException, MessagingException;
}

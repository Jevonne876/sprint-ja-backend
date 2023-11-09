package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.enums.Role;
import com.example.sprintjabackend.exception.domain.EmailExistException;
import com.example.sprintjabackend.exception.domain.EmailNotFoundException;
import com.example.sprintjabackend.exception.domain.PhoneNumberException;
import com.example.sprintjabackend.exception.domain.TrnExistException;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.model.UserPrincipal;
import com.example.sprintjabackend.repository.UserRepository;
import com.example.sprintjabackend.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

import static com.example.sprintjabackend.constant.UserImplementationConstant.*;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.emailService = emailService;
    }


    @Override
    public User register(Long trn, String firstName, String lastName,
                         Date dateOfBirth, String email, String password, String phoneNumber,
                         String streetAddress, String parish, String pickUpBranch) throws PhoneNumberException, EmailExistException, TrnExistException, MessagingException {

        validateTrnAndEmail(trn, email, phoneNumber);
        User newUser = new User();
        newUser.setTrn(trn);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setDateOfBirth(dateOfBirth);
        newUser.setEmail(email);
        newUser.setUsername(email);
        newUser.setPassword(encoder.encode(password));
        newUser.setPhoneNumber(phoneNumber);
        newUser.setStreetAddress(streetAddress);
        newUser.setParish(parish);
        newUser.setPickUpBranch(pickUpBranch);
        this.userRepository.save(newUser);
        this.emailService.newUserEmail(firstName, email);
        return newUser;
    }

    @Override
    public User addNewUserFromAdmin(Long trn,
                                    String firstName, String lastName,
                                    Date dateOfBirth, String email, String phoneNumber,
                                    String streetAddress, String parish,
                                    String pickUpBranch) throws PhoneNumberException, EmailExistException, TrnExistException, MessagingException {
        User newUser = new User();
        String password = generatePassword();
        newUser.setTrn(0L);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setDateOfBirth(new Date());
        newUser.setEmail(email);
        newUser.setUsername(email);
        newUser.setPassword(encoder.encode(password));
        newUser.setPhoneNumber(phoneNumber);
        newUser.setStreetAddress("");
        newUser.setParish("");
        newUser.setPickUpBranch("");
        this.userRepository.save(newUser);
        this.emailService.sendNewAccountPassword(firstName, lastName, password, email);
        return null;
    }

    @Override
    public User updateUser(UUID userId, Long newTrn, String newFirstName, String newLastName,
                           String newEmail, String newPhoneNumber,
                           String newAddress1, String newAddress2, String newPickUpBranch)
            throws EmailExistException, TrnExistException, PhoneNumberException {

        User user = findUserByUserId(userId);

        if (user.getTrn().equals(newTrn) && user.getEmail().equals(newEmail) && user.getPhoneNumber().equals(newPhoneNumber)) {
            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            user.setStreetAddress(newAddress1);
            user.setParish(newAddress2);
            user.setPickUpBranch(newPickUpBranch);
            return this.userRepository.save(user);
        } else if (!user.getTrn().equals(newTrn) && user.getEmail().equals(newEmail) && user.getPhoneNumber().equals(newPhoneNumber)) {

            user.setTrn(newTrn);
            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            user.setPhoneNumber(newPhoneNumber);
            user.setStreetAddress(newAddress1);
            user.setParish(newAddress2);
            user.setPickUpBranch(newPickUpBranch);
            return this.userRepository.save(user);
        } else if (user.getTrn().equals(newTrn) && !user.getEmail().equals(newEmail) && user.getPhoneNumber().equals(newPhoneNumber)) {

            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            user.setEmail(newEmail);
            user.setUsername(newEmail);
            user.setStreetAddress(newAddress1);
            user.setParish(newAddress2);
            user.setPickUpBranch(newPickUpBranch);
            return this.userRepository.save(user);

        } else if (user.getTrn().equals(newTrn) && user.getEmail().equals(newEmail) && !user.getPhoneNumber().equals(newPhoneNumber)) {

            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            user.setPhoneNumber(newPhoneNumber);
            user.setStreetAddress(newAddress1);
            user.setParish(newAddress2);
            user.setPickUpBranch(newPickUpBranch);
            return this.userRepository.save(user);
        } else {

            user.setTrn(newTrn);
            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            user.setEmail(newEmail);
            user.setUsername(newEmail);
            user.setPhoneNumber(newPhoneNumber);
            user.setStreetAddress(newAddress1);
            user.setParish(newAddress2);
            user.setPickUpBranch(newPickUpBranch);
            return this.userRepository.save(user);
        }
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    @Override
    public User findUserByTrn(Long trn) {
        return this.userRepository.findUserByTrn(trn);
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        return this.userRepository.findUserByPhoneNumber(phoneNumber);
    }

    @Override
    public User findUserByUserId(UUID userId) {
        return userRepository.findUserByUserId(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_EMAIL + username);
        } else {
            userRepository.save(user);
            return new UserPrincipal(user);
        }
    }

    @Override
    public void resetPassword(String email) throws EmailNotFoundException, MessagingException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
        this.emailService.sendNewPasswordEmail(user.getFirstName(), password, user.getEmail());

    }

    @Override
    public Boolean passwordReset(String email) throws EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return true;
        }

        return false;

    }


    @Override
    public Page<User> findAllByRoleAndLastNameContainingIgnoreCaseOrderByCreatedAtDesc(Pageable pageable, String lastName) {
        return userRepository.findAllByRoleAndLastNameContainingIgnoreCaseOrderByCreatedAtDesc(pageable, Role.ROLE_USER.toString(), lastName);
    }


    public Page<User> findAllAdminByRole(Pageable pageable) {
        return userRepository.findAllByRoleOrderByUpdatedAtDesc(pageable, Role.ROLE_SUPER_ADMIN.toString());
    }


    @Override
    public void deleteUser(String username) {
        User user = userRepository.findUserByUsername(username);
        userRepository.deleteById(user.getId());
    }


    private void validateTrnAndEmail(long newTrn, String newEmail, String newPhoneNumber) throws TrnExistException, EmailExistException, PhoneNumberException {

        User userByNewTrn = findUserByTrn(newTrn);
        User userByNewEmail = findUserByEmail(newEmail);
        User userByNewPhoneNumber = findUserByPhoneNumber(newPhoneNumber);


        if (userByNewTrn != null) {
            throw new TrnExistException(TRN_ALREADY_EXISTS);
        } else if (userByNewEmail != null) {
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        } else if (userByNewPhoneNumber != null) {
            throw new PhoneNumberException(PHONE_NUMBER_ALREADY_EXISTS);

        }

    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

}

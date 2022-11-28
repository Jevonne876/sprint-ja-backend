package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.exception.domain.EmailExistException;
import com.example.sprintjabackend.exception.domain.PhoneNumberException;
import com.example.sprintjabackend.exception.domain.TrnExistException;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.model.UserPrincipal;
import com.example.sprintjabackend.repository.UserRepository;
import com.example.sprintjabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

import static com.example.sprintjabackend.constant.UserImplementationConstant.*;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }


    @Override
    public User register(Long trn, String firstName, String lastName,
                         Date dateOfBirth, String email, String password, String phoneNumber,
                         String streetAddress, String parish, String pickUpBranch) throws PhoneNumberException, EmailExistException, TrnExistException {

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
        return this.userRepository.save(newUser);
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_EMAIL + username);
        } else {
            userRepository.save(user);
            return new UserPrincipal(user);
        }
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
}

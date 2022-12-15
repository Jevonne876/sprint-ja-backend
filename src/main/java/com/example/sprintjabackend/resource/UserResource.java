package com.example.sprintjabackend.resource;

import com.example.sprintjabackend.exception.domain.EmailExistException;
import com.example.sprintjabackend.exception.domain.EmailNotFoundException;
import com.example.sprintjabackend.exception.domain.PhoneNumberException;
import com.example.sprintjabackend.exception.domain.TrnExistException;
import com.example.sprintjabackend.model.HttpResponse;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.model.UserPrincipal;
import com.example.sprintjabackend.service.UserService;
import com.example.sprintjabackend.utility.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.UUID;

import static com.example.sprintjabackend.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/")
public class UserResource {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserResource(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    @PostMapping(value = "register-new-user")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws EmailExistException, TrnExistException, PhoneNumberException, MessagingException {
        User newUser = new User();
        newUser = userService.register(user.getTrn(), user.getFirstName(), user.getLastName(),
                user.getDateOfBirth(), user.getEmail(), user.getPassword(),
                user.getPhoneNumber(), user.getStreetAddress(), user.getParish(), user.getPickUpBranch());
        return new ResponseEntity<>(newUser, OK);
    }

    @PutMapping(value = "update-user")
    public ResponseEntity<User> updatedUser(@RequestParam UUID userId,
                                            @RequestParam Long trn,
                                            @RequestParam String newFirstName,
                                            @RequestParam String newLastName,
                                            @RequestParam Date newDateOfBirth,
                                            @RequestParam String newEmail,
                                            @RequestParam String newPassword,
                                            @RequestParam String newPhoneNumber,
                                            @RequestParam String newAddress1,
                                            @RequestParam String newAddress2,
                                            @RequestParam String newPickUpBranch
    ) throws TrnExistException, EmailExistException, PhoneNumberException {

        return new ResponseEntity<>(userService.updateUser(userId, trn, newFirstName,
                newLastName, newDateOfBirth, newEmail, newPassword,
                newPhoneNumber, newAddress1, newAddress2, newPickUpBranch), OK);
    }

    @PostMapping(value = "user-login")
    public ResponseEntity<User> userLogin(@RequestBody User user) {
        authenticate(user.getEmail(), user.getPassword());
        User loggedInUser = new User();
        loggedInUser = userService.findUserByUsername(user.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loggedInUser);
        HttpHeaders httpHeaders = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loggedInUser, httpHeaders, OK);
    }



    @GetMapping("/reset-password/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws MessagingException, EmailNotFoundException {
        userService.resetPassword(email);
        return response(OK, "" + email);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }


}

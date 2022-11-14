package com.example.sprintjabackend.resource;

import com.example.sprintjabackend.exception.domain.EmailExistException;
import com.example.sprintjabackend.exception.domain.UserNotFoundException;
import com.example.sprintjabackend.exception.domain.UsernameExistException;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.model.UserPrincipal;
import com.example.sprintjabackend.service.UserService;
import com.example.sprintjabackend.utility.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<User> registerUser(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException {
        User newUser = new User();
        newUser = userService.register(user.getTrn(), user.getFirstName(), user.getLastName(),
                user.getDateOfBirth(), user.getEmail(), user.getPassword(),
                user.getPhoneNumber(), user.getAddress1(), user.getAddress2(), user.getPickUpBranch());
        return new ResponseEntity<>(newUser, OK);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }
}

package com.example.sprintjabackend.resource;

import com.example.sprintjabackend.exception.domain.UsernameExistException;
import com.example.sprintjabackend.model.ApplicationInfo;
import com.example.sprintjabackend.model.HttpResponse;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.model.UserPrincipal;
import com.example.sprintjabackend.repository.UserRepository;
import com.example.sprintjabackend.service.AdminService;
import com.example.sprintjabackend.service.PackageService;
import com.example.sprintjabackend.service.UserService;
import com.example.sprintjabackend.utility.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.sprintjabackend.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/")
public class AdminResource {

    private AdminService adminService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    private final PackageService packageService;

    @Autowired
    public AdminResource(AdminService adminService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                         UserRepository userRepository, UserService userService, PackageService packageService) {
        this.adminService = adminService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.userService = userService;
        this.packageService = packageService;
    }

    @PostMapping(value = "/admin/register-new-admin")
    public ResponseEntity<User> registerNewAdmin(@RequestBody User user) throws UsernameExistException {
        User newAdminUser = new User();
        newAdminUser = adminService.registerNewAdmin(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword());
        return new ResponseEntity<>(newAdminUser, HttpStatus.OK);
    }

    @PostMapping(value = "/admin/admin-login")
    public ResponseEntity<User> adminLogin(@RequestBody User user) {
        authenticate(user.getEmail(), user.getPassword());
        User loggedInUser = new User();
        loggedInUser = adminService.findAdminByUsername(user.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loggedInUser);
        HttpHeaders httpHeaders = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loggedInUser, httpHeaders, OK);
    }

    @GetMapping(value = "/admin/get-application-data")
    public ResponseEntity<ApplicationInfo> getTotalData() {
        return new ResponseEntity<>(adminService.getData(), OK);
    }

    @GetMapping(value = "/admin/get-all-users")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(userService.findAllByRole(pageable), OK);
    }

    @GetMapping(value = "/admin/get-all-user-packages")
    public ResponseEntity<Page<Package>> getAllUserPackages(@RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(packageService.findAll(pageable), OK);
    }

    @GetMapping(value = "/admin/get-all-user-packages-not-shipped")
    public ResponseEntity<Page<Package>> getAllUserPackagesNotShipped(@RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(packageService.findAllNotShipped(pageable), OK);
    }

    @GetMapping(value = "/admin/get-all-user-packages-shipped")
    public ResponseEntity<Page<Package>> getAllUserPackagesShipped(@RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(packageService.findAllShipped(pageable), OK);
    }

    @GetMapping(value = "/admin/get-all-user-packages-ready")
    public ResponseEntity<Page<Package>> getAllUserPackagesReadyForPickup(@RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(packageService.findAllReadyForPickup(pageable), OK);
    }

    @GetMapping(value = "/admin/get-all-user-packages-delivered")
    public ResponseEntity<Page<Package>> getAllUserPackagesDelivered(@RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(packageService.findAllDelivered(pageable), OK);
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

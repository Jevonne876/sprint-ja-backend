package com.example.sprintjabackend.resource;

import com.example.sprintjabackend.enums.Role;
import com.example.sprintjabackend.exception.domain.*;
import com.example.sprintjabackend.model.*;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.repository.AdminRepository;
import com.example.sprintjabackend.repository.UserRepository;
import com.example.sprintjabackend.service.AdminService;
import com.example.sprintjabackend.service.FileStore;
import com.example.sprintjabackend.service.PackageService;
import com.example.sprintjabackend.service.UserService;
import com.example.sprintjabackend.service.implementation.EmailService;
import com.example.sprintjabackend.service.implementation.ReportsGenerator;
import com.example.sprintjabackend.utility.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.sprintjabackend.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static java.nio.file.Paths.get;
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
    private final EmailService emailService;

    private final FileStore fileStore;

    private final ReportsGenerator reportsGenerator;
    private final AdminRepository adminRepository;


    @Autowired
    public AdminResource(AdminService adminService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                         UserRepository userRepository, UserService userService, PackageService packageService, EmailService emailService, FileStore fileStore, ReportsGenerator reportsGenerator,
                         AdminRepository adminRepository) {
        this.adminService = adminService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.userService = userService;
        this.packageService = packageService;
        this.emailService = emailService;
        this.fileStore = fileStore;
        this.reportsGenerator = reportsGenerator;
        this.adminRepository = adminRepository;
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

    @PostMapping(value = "/admin/register-new-user")
    public ResponseEntity<User> registerNewUser(@RequestBody User user) throws PhoneNumberException, EmailExistException, MessagingException, TrnExistException {
        User newUser = new User();


        newUser = userService.addNewUserFromAdmin(user.getTrn(),
                user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getEmail(),
                user.getPhoneNumber(), user.getStreetAddress(), user.getParish(),
                user.getPickUpBranch());

        return new ResponseEntity<>(newUser, OK);

    }

    @PostMapping(value = "/admin/create-new-pre-alert")
    public ResponseEntity<Package> addNewPackage(@RequestParam String trackingNumber,
                                                 @RequestParam String courier,
                                                 @RequestParam String description,
                                                 @RequestParam String status,
                                                 @RequestParam double weight,
                                                 @RequestParam double cost,
                                                 @RequestParam UUID userId,
                                                 @RequestParam("file") MultipartFile multipartFile
    ) throws TrackingNumberException, MessagingException, IOException {
        Package newPackage;

        newPackage = packageService.addNewPackage(trackingNumber, courier, description, status, weight, cost, userId, multipartFile);

        return new ResponseEntity<>(newPackage, OK);
    }

    @PostMapping("/admin/send-email")
    public ResponseEntity<HttpResponse> sendEmail(
            @RequestBody Email email) throws MessagingException {
        emailService.sendMessage(email.getRecipient(), email.getSubject(), email.getMessage());
        return response(OK, "Email Successfully Sent");
    }

    @PostMapping("/admin/send-broadcast-email")
    public ResponseEntity<HttpResponse> sendBroadCastEmail(
            @RequestParam("subject") String subject, @RequestParam("message") String message) throws MessagingException {
        List<String> emails = adminService.findAllEmails();
        emailService.sendBroadCastMessage(emails, subject, message);
        return response(OK, "Email Successfully Sent");
    }

    @PutMapping(value = "/admin/update-package/{oldTrackingNumber}")
    public ResponseEntity<Package> updatePackage(@RequestBody Package apackage
            , @PathVariable("oldTrackingNumber") String oldTrackingNumber) throws TrackingNumberException, IOException, MessagingException {
        Package updatedPackageData = new Package();

        updatedPackageData.setTrackingNumber(apackage.getTrackingNumber());
        updatedPackageData.setCourier(apackage.getCourier());
        updatedPackageData.setDescription(apackage.getDescription());
        updatedPackageData.setWeight(apackage.getWeight());
        updatedPackageData.setCost(apackage.getCost());
        updatedPackageData.setStatus(apackage.getStatus());
        updatedPackageData.setUserId(apackage.getUserId());
        MultipartFile file = null;

        return new ResponseEntity<>(packageService.adminUpdatePackage(oldTrackingNumber, apackage.getTrackingNumber(),
                apackage.getCourier(), apackage.getDescription(), apackage.getStatus(), apackage.getWeight(),
                apackage.getCost(), apackage.getUserId()), OK);
    }

    @PutMapping(value = "/admin/file-upload/{trackingNumber}")
    public ResponseEntity<HttpResponse> fileUpload(@PathVariable("trackingNumber") String trackingNumber, @RequestParam MultipartFile file) throws IOException {

        Package aPackage = packageService.findByTrackingNumber(trackingNumber);

        if (!aPackage.getInvoice().equals("") || !aPackage.getInvoice().equals(null)) {
            fileStore.deleteFile(aPackage.getInvoice());
        }

        User user = userService.findUserByUserId(aPackage.getUserId());

        String fileName = packageService.fileUpload(trackingNumber, file);

        aPackage.setInvoice(fileName);
        packageService.update(aPackage);

        return response(OK, "File uploaded");
    }



    @GetMapping(value = "/admin/view-package/{trackingNumber}")
    public ResponseEntity<Package> viewPackage(@PathVariable("trackingNumber") String trackingNumber) {

        return new ResponseEntity<>(packageService.findByTrackingNumber(trackingNumber), OK);
    }

    @GetMapping(value = "/admin/view-user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") UUID userId) {

        return new ResponseEntity<>(userService.findUserByUserId(userId), OK);
    }

    @GetMapping(value = "/admin/get-application-data")
    public ResponseEntity<ApplicationInfo> getTotalData() {
        return new ResponseEntity<>(adminService.getData(), OK);
    }

    @GetMapping(value = "/admin/get-all-users")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam Optional<Integer> page, @RequestParam Optional<String> firstName) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(userService.findAllByRoleAndLastNameContainingIgnoreCaseOrderByCreatedAtDesc(pageable,firstName.orElse("")), OK);
    }

    @GetMapping(value = "/admin/get-all-admin-users")
    public ResponseEntity<Page<User>> getAllAdminUsers(@RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(userService.findAllAdminByRole(pageable), OK);
    }

    @GetMapping(value = "/admin/get-all-user-packages")
    public ResponseEntity<Page<Package>> getAllUserPackages(@RequestParam Optional<Integer> page, @RequestParam Optional<String> trackingNumber) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(packageService.findAllByTrackingNumberContainingIgnoreCaseOrderByUpdatedAtAsc(pageable,trackingNumber.orElse("")), OK);
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

    @GetMapping("/admin/invoice-download/{filename}")
    public ResponseEntity<byte[]> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", MediaType.ALL_VALUE);
        headers.add("Content-Disposition", "attachment; filename=" + filename);
        return new ResponseEntity<>(fileStore.downloadFile(filename), headers, OK);
    }

    @GetMapping("/get-emails")
    public ResponseEntity<List<String>> getEmails() {
        return new ResponseEntity<>(adminService.findAllEmails(), OK);
    }


    @GetMapping("/admin/export-users")
    public void generateExcelReport(HttpServletResponse response) throws Exception{

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=SprintJA-Customers.xls";

        response.setHeader(headerKey, headerValue);

        reportsGenerator.generateExcel(response);

        response.flushBuffer();
    }

    @GetMapping("/admin/export-packages")
    public void generateExcelReportPackages(HttpServletResponse response) throws Exception{

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=SprintJA-Packages.xls";

        response.setHeader(headerKey, headerValue);

        reportsGenerator.generateExcelPackages(response);
        response.flushBuffer();
    }

    @PutMapping(value = "/admin/update-user")
    public ResponseEntity<User> updatedUser(@RequestParam UUID userId,
                                            @RequestParam Long trn,
                                            @RequestParam String newFirstName,
                                            @RequestParam String newLastName,
                                            @RequestParam String newEmail,
                                            @RequestParam String newPhoneNumber,
                                            @RequestParam String newAddress1,
                                            @RequestParam String newAddress2,
                                            @RequestParam String newPickUpBranch
    ) throws TrnExistException, EmailExistException, PhoneNumberException {

        return new ResponseEntity<>(userService.updateUser(userId, trn, newFirstName,
                newLastName, newEmail,
                newPhoneNumber, newAddress1, newAddress2, newPickUpBranch), OK);
    }

    @DeleteMapping("/admin/delete-user/{username}")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws IOException {
        userService.deleteUser(username);
        return response(OK, "User Deleted Successfully");
    }

    @DeleteMapping("/admin/delete-package/{trackingNumber}")
    public ResponseEntity<HttpResponse> deletePackage(@PathVariable("trackingNumber") String trackingNumber) throws IOException {

        packageService.deletePackage(trackingNumber);
        return response(OK, "User Deleted Successfully");
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

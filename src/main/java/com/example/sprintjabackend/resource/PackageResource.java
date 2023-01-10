package com.example.sprintjabackend.resource;


import com.example.sprintjabackend.exception.domain.TrackingNumberException;
import com.example.sprintjabackend.model.HttpResponse;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.model.UserPackageInfo;
import com.example.sprintjabackend.service.PackageService;
import com.example.sprintjabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static com.example.sprintjabackend.service.implementation.PackageServiceImpl.DIRECTORY;
import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/api/v1/")
public class PackageResource {

    private final PackageService packageService;
    private final UserService userService;

    @Autowired
    public PackageResource(PackageService packageService, UserService userService) {
        this.packageService = packageService;
        this.userService = userService;
    }

    @GetMapping(value = "get-all-packages")
    public ResponseEntity<Page<Package>> getAll() {
        Pageable pageable = PageRequest.of(0, 10);
        return new ResponseEntity<>(packageService.findAll(pageable), OK);
    }

    @GetMapping(value = "total-packages/{userId}")
    public ResponseEntity<UserPackageInfo> getTotalPackageCount(@PathVariable("userId") UUID userId) {
        return new ResponseEntity<>(packageService.getFinalCount(userId), OK);
    }

    @GetMapping(value = "/get-all-user-packages-not-shipped/{userId}")
    public ResponseEntity<Page<Package>> getAllUserPackagesNotShipped(@PathVariable("userId") UUID userId, @RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(packageService.findAllUserPackagesNotShipped(userId, pageable), OK);
    }

    @GetMapping(value = "/get-all-user-packages-shipped/{userId}")
    public ResponseEntity<Page<Package>> getAllUserPackagesShipped(@PathVariable("userId") UUID userId, @RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(packageService.findAllUserPackagesShipped(userId, pageable), OK);
    }

    @GetMapping(value = "/get-all-user-packages-ready/{userId}")
    public ResponseEntity<Page<Package>> getAllUserPackagesReadyForPickup(@PathVariable("userId") UUID userId, @RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(packageService.findAllUserPackagesReadyForPickup(userId, pageable), OK);
    }

    @GetMapping(value = "/get-all-user-packages-delivered/{userId}")
    public ResponseEntity<Page<Package>> getAllUserPackagesDelivered(@PathVariable("userId") UUID userId, @RequestParam Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        return new ResponseEntity<>(packageService.findAllUserPackagesDelivered(userId, pageable), OK);
    }

    @GetMapping(value = "/get-user-package-by-tracking-number/{trackingNumber}")
    public ResponseEntity<Package> getUserPackageByTrackingNumber(@PathVariable("trackingNumber") String trackingNumber) {
        return new ResponseEntity<>(packageService.findByTrackingNumber(trackingNumber), OK);
    }

    @PostMapping(value = "add-new-package")
    public ResponseEntity<Package> addNewPackage(@RequestParam String trackingNumber,
                                                 @RequestParam String courier,
                                                 @RequestParam String description,
                                                 @RequestParam double weight,
                                                 @RequestParam double cost,
                                                 @RequestParam UUID userId,
                                                 @RequestParam("file") MultipartFile multipartFile
    ) throws TrackingNumberException, MessagingException, IOException {
        Package newPackage;

        newPackage = packageService.addNewPackage(trackingNumber, courier, description, weight, cost, userId, multipartFile);

        return new ResponseEntity<>(newPackage, OK);
    }

    @PutMapping(value = "update-package/{oldTrackingNumber}")
    public ResponseEntity<Package> updatePackage(@RequestBody Package apackage
            , @PathVariable("oldTrackingNumber") String oldTrackingNumber) throws TrackingNumberException {
        Package updatedPackageData = new Package();

        updatedPackageData.setTrackingNumber(apackage.getTrackingNumber());
        updatedPackageData.setCourier(apackage.getCourier());
        updatedPackageData.setDescription(apackage.getDescription());
        updatedPackageData.setWeight(apackage.getWeight());
        updatedPackageData.setCost(apackage.getCost());
        updatedPackageData.setUserId(apackage.getUserId());

        return new ResponseEntity<>(packageService.updatePackage(oldTrackingNumber, apackage.getTrackingNumber(),
                apackage.getCourier(), apackage.getDescription(), apackage.getWeight(),
                apackage.getCost(), apackage.getUserId()), OK);


    }

    @GetMapping(value="view-package/{trackingNumber}")
    public ResponseEntity<Package> viewPackage(@PathVariable("trackingNumber") String trackingNumber){
        return new ResponseEntity<>(packageService.findByTrackingNumber(trackingNumber), OK);
    }

    @PutMapping(value = "invoice-upload/{trackingNumber}")
    public ResponseEntity<HttpResponse> fileUpload(@PathVariable("trackingNumber") String trackingNumber, @RequestParam MultipartFile file) throws IOException {

        Package aPackage = packageService.findByTrackingNumber(trackingNumber);

        User user = userService.findUserByUserId(aPackage.getUserId());

        String fileName = packageService.fileUpload(trackingNumber, file);

        aPackage.setInvoice(fileName);
        packageService.update(aPackage);

        return  response(OK,"File uploaded Successfully");
    }


    @GetMapping("invoice-download/{filename}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(filename + " was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

}

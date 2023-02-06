package com.example.sprintjabackend.resource;


import com.example.sprintjabackend.exception.domain.TrackingNumberException;
import com.example.sprintjabackend.model.HttpResponse;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.model.UserPackageInfo;
import com.example.sprintjabackend.repository.PackageRepository;
import com.example.sprintjabackend.service.FileStore;
import com.example.sprintjabackend.service.PackageService;
import com.example.sprintjabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/api/v1/")
public class PackageResource {

    private final PackageService packageService;
    private final UserService userService;
    private final FileStore fileStore;
    private final PackageRepository packageRepository;

    @Autowired
    public PackageResource(PackageService packageService, UserService userService, FileStore fileStore,
                           PackageRepository packageRepository) {
        this.packageService = packageService;
        this.userService = userService;
        this.fileStore = fileStore;
        this.packageRepository = packageRepository;
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

    @GetMapping(value = "view-package/{trackingNumber}")
    public ResponseEntity<Package> viewPackage(@PathVariable("trackingNumber") String trackingNumber) {
        return new ResponseEntity<>(packageService.findByTrackingNumber(trackingNumber), OK);
    }

    @PutMapping(value = "invoice-upload/{trackingNumber}")
    public ResponseEntity<HttpResponse> fileUpload(@PathVariable("trackingNumber") String trackingNumber, @RequestParam MultipartFile file) throws IOException {

        Package aPackage = packageService.findByTrackingNumber(trackingNumber);

        if (!aPackage.getInvoice().equals("") || !aPackage.getInvoice().equals(null)) {
            fileStore.deleteFile(aPackage.getInvoice());
        }
        User user = userService.findUserByUserId(aPackage.getUserId());

        String fileName = fileStore.uploadFile(file);

        aPackage.setInvoice(fileName);
        packageService.update(aPackage);

        return response(OK, "File uploaded Successfully");
    }




    @GetMapping("invoice-download/{filename}")
    public ResponseEntity<byte[]> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", MediaType.ALL_VALUE);
        headers.add("Content-Disposition", "attachment; filename=" + filename);
        return new ResponseEntity<>(fileStore.downloadFile(filename), headers, OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

}

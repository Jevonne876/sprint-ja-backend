package com.example.sprintjabackend.resource;

import com.example.sprintjabackend.enums.PackageStatus;
import com.example.sprintjabackend.exception.domain.FileExtensionException;
import com.example.sprintjabackend.exception.domain.TrackingNumberException;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.UserPackageInfo;
import com.example.sprintjabackend.service.PackageService;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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

    @Autowired
    public PackageResource(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping(value = "get-all-packages")
    public ResponseEntity<Page<Package>> getAll() {
        Pageable pageable = PageRequest.of(0, 10);
        return new ResponseEntity<>(packageService.findAll(pageable), OK);
    }

//    @GetMapping(value = "get-all-packages/{userId}")
//    public ResponseEntity<List<Package>> getAllPackagesByUserId(@PathVariable("userId") UUID userId) {
//        return new ResponseEntity<>(packageService.findAllByUserIdAndStatusOrderByCreatedAtDesc(userId, PackageStatus.DEFAULT.toString()), OK);
//    }

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

    @PutMapping(value = "update-package")
    public ResponseEntity<Package> updatePackage(@RequestParam String trackingNumber,
                                                 @RequestParam String courier,
                                                 @RequestParam String description,
                                                 @RequestParam double weight,
                                                 @RequestParam double cost,
                                                 @RequestParam UUID userId,
                                                 @RequestParam String oldTrackingNumber) throws TrackingNumberException {
        Package updatedPackageData = new Package();

        updatedPackageData.setTrackingNumber(trackingNumber);
        updatedPackageData.setCourier(courier);
        updatedPackageData.setDescription(description);
        updatedPackageData.setWeight(weight);
        updatedPackageData.setCost(cost);
        updatedPackageData.setUserId(userId);

        return new ResponseEntity<>(packageService.updatePackage(oldTrackingNumber, trackingNumber, courier, description, weight, cost, userId), OK);


    }

    @PostMapping(value = "invoice-upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException, FileExtensionException {

        return new ResponseEntity<>(packageService.fileUpload("123", multipartFile), OK);
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


}

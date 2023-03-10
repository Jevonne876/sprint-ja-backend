package com.example.sprintjabackend.resource;

import com.example.sprintjabackend.enums.PackageStatus;
import com.example.sprintjabackend.exception.domain.TrackingNumberException;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.UUID;

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

    @GetMapping(value = "get-all-packages/{userId}")
    public ResponseEntity<List<Package>> getAllPackagesByUserId(@PathVariable("userId") UUID userId) {

        return new ResponseEntity<>(packageService.findAllByUserIdAndStatusOrderByCreatedAtDesc(userId, PackageStatus.DEFAULT.toString()), OK);

    }


    @GetMapping(value = "total-package")
    public ResponseEntity<Package> getTotalPackageCount() {
        return new ResponseEntity<>(packageService.getFinalCount(), OK);
    }

//    @GetMapping(value = "get-all-packages/{userId}")
//    public ResponseEntity<Page<Package>> getAllPackagesByUserId(@PathVariable("userId") UUID userId) {
//        Pageable pageable = PageRequest.of(0, 10);
//        return new ResponseEntity<>(packageService.findAllPackageByUserId(userId, pageable), OK);
//
//    }

    @PostMapping(value = "add-new-package")
    public ResponseEntity<Package> addNewPackage(@RequestBody Package data) throws TrackingNumberException, MessagingException {
        Package newPackage;
        newPackage = packageService.addNewPackage(data.getTrackingNumber(), data.getCourier(),
                data.getDescription(), data.getWeight(), data.getCost(), data.getUserId());

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

}

package com.example.sprintjabackend.resource;

import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
        Pageable pageable = PageRequest.of(0, 1);
        System.out.println(packageService.findAll(pageable));
        return new ResponseEntity<>(packageService.findAll(pageable), OK);
    }

    @PostMapping(value = "add-new-package")
    public ResponseEntity<Package> addNewPackage(@RequestBody Package data) {
        Package newPackage = new Package();
        newPackage = packageService.addNewPackage(data.getTrackingNumber(),
                data.getDescription(), data.getWeight(), data.getCost(), data.getUser());

        return new ResponseEntity<>(newPackage, OK);
    }

}

package com.example.sprintjabackend.service;

import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PackageService {

    Package addNewPackage(String trackingNumber, String description,
                          double weight, double cost, User user);

    Page<Package> findAll(Pageable pageable);

}

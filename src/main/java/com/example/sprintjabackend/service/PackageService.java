package com.example.sprintjabackend.service;

import com.example.sprintjabackend.exception.domain.TrackingNumberException;
import com.example.sprintjabackend.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PackageService {

    Package addNewPackage(String trackingNumber, String description,
                          double weight, double cost, UUID userId) throws TrackingNumberException;

    Page<Package> findAll(Pageable pageable);

    Page<Package> findAllPackageByUserId(UUID userId, Pageable pageable);

    Package findByTrackingNumber(String trackingNumber);


    Package updatePackage(String oldPackageNumber, String trackingNumber,
                          String description, double weight,
                          double cost, UUID userId) throws TrackingNumberException;
}

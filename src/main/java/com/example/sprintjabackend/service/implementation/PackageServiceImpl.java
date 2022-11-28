package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.repository.PackageRepository;
import com.example.sprintjabackend.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public Package addNewPackage(String trackingNumber, String description, double weight, double cost, UUID userId) {
        Package aPackage = new Package();
        aPackage.setTrackingNumber(trackingNumber);
        aPackage.setDescription(description);
        aPackage.setWeight(weight);
        aPackage.setCost(cost);
        aPackage.setUserId(userId);
        return packageRepository.save(aPackage);
    }

    @Override
    public Page<Package> findAll(Pageable pageable) {
        return packageRepository.findAll(pageable);
    }

    @Override
    public Page<Package> findAllPackageByUserId(UUID userId, Pageable pageable) {
        return packageRepository.findAllPackageByUserId(userId, pageable);
    }

    @Override
    public Package findByTrackingNumber(String trackingNumber) {
        return packageRepository.findByTrackingNumber(trackingNumber);
    }


}

package com.example.sprintjabackend.repository;

import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, Long> {

    Page<Package> findAll(Pageable pageable);

    Page<Package> findAllPackageByUserId(UUID userId, Pageable pageable);

    Package findByTrackingNumber(String trackingNumber);




}

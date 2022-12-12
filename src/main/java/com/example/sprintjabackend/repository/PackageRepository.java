package com.example.sprintjabackend.repository;

import com.example.sprintjabackend.enums.PackageStatus;
import com.example.sprintjabackend.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PackageRepository extends CrudRepository <Package, Long> {


    Page<Package> findAll(Pageable pageable);

    Page<Package> findAllPackageByUserId(UUID userId, Pageable pageable);

    Package findByTrackingNumber(String trackingNumber);

    Long countByStatus(String status);

    List<Package> findByUserIdAndStatus(UUID uuid, String status);


}

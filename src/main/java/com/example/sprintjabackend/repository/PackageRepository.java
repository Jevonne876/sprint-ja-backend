package com.example.sprintjabackend.repository;

import com.example.sprintjabackend.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PackageRepository extends CrudRepository<Package, Long> {


    Page<Package> findAll(Pageable pageable);

    Page<Package> findAllPackageByUserId(UUID userId, Pageable pageable);

    Page<Package> findAllByStatus(Pageable pageable, String status);

    Package findByTrackingNumber(String trackingNumber);

    Long countByUserIdAndStatus(UUID userId, String status);

    Long countByStatus(String Status);


    Page<Package> findAllByUserIdAndStatusOrderByUpdatedAtDesc(UUID userId,String status, Pageable pageable);

    List<Package> findAllByUserIdAndStatusOrderByCreatedAtDesc(UUID uuid, String status);

}

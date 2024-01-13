package com.example.sprintjabackend.repository;

import com.example.sprintjabackend.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PackageRepository extends CrudRepository<Package, Long> {


    Page<Package> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Package> findAllByTrackingNumberContainingIgnoreCaseOrderByUpdatedAtDesc(Pageable pageable, String trackingNumber);

    Page<Package> findAllPackageByUserId(UUID userId, Pageable pageable);

    Page<Package> findAllByStatusOrderByCreatedAtDesc(Pageable pageable, String status);

    Package findByTrackingNumber(String trackingNumber);

    Long countByUserIdAndStatus(UUID userId, String status);

    Long countByStatus(String Status);


    Page<Package> findAllByUserIdAndStatusOrderByUpdatedAtDesc(UUID userId,String status, Pageable pageable);

    List<Package> findAllByUserIdAndStatusOrderByCreatedAtDesc(UUID uuid, String status);


    List<Package> findAllByOrderByCreatedAtAsc();
}

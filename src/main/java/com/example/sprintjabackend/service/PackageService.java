package com.example.sprintjabackend.service;

import com.example.sprintjabackend.exception.domain.TrackingNumberException;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.UserPackageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PackageService {

    Package addNewPackage(String trackingNumber, String courier, String description,
                          double weight, double cost, UUID userId, MultipartFile file) throws TrackingNumberException, MessagingException, IOException;

    Package addNewPackage(String trackingNumber, String courier, String description,String status,
                          double weight, double cost, UUID userId, MultipartFile file) throws TrackingNumberException, MessagingException, IOException;

    Page<Package> findAll(Pageable pageable);

    Page<Package> findAllPackageByUserId(UUID userId, Pageable pageable);

    Package findByTrackingNumber(String trackingNumber);

    List<Package> findAllByUserIdAndStatusOrderByCreatedAtDesc(UUID uuid, String Status);

    Package updatePackage(String oldPackageNumber, String trackingNumber, String courier,
                          String description, double weight,
                          double cost, UUID userId) throws TrackingNumberException;

    Package adminUpdatePackage(String oldTrackingNumber, String trackingNumber, String courier,
                          String description,String status, double weight,
                          double cost, UUID userId) throws TrackingNumberException, IOException, MessagingException;


    Long countByUserIdAndStatus(UUID userId, String status);

    UserPackageInfo getFinalCount(UUID userId);

    Long packagesNotShipped();

    Long packagesShipped();

    Long packagesReadyForPickup();

    Long packagesDelivered();

    String fileUpload(String packageTrackingNumber, MultipartFile multipartFile) throws IOException;

    Long userPackagesNotShipped(UUID uuid);

    Long userPackagesShipped(UUID uuid);

    Long userPackagesReadyForPickup(UUID uuid);

    Long userPackagesDelivered(UUID uuid);

    Page<Package> findAllUserPackagesNotShipped(UUID userId,Pageable pageable);

    Page<Package> findAllUserPackagesShipped(UUID userId,Pageable pageable);

    Page<Package> findAllUserPackagesReadyForPickup(UUID userId,Pageable pageable);

    Page<Package> findAllUserPackagesDelivered(UUID userId,Pageable pageable);

    Page<Package> findAllNotShipped(Pageable pageable);

    Page<Package> findAllShipped(Pageable pageable);

    Page<Package> findAllReadyForPickup(Pageable pageable);

    Page<Package> findAllDelivered(Pageable pageable);

    void deletePackage(String trackingNumber);

    Package update ( Package apackage);



}

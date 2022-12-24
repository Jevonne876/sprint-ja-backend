package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.enums.PackageStatus;
import com.example.sprintjabackend.exception.domain.TrackingNumberException;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.repository.PackageRepository;
import com.example.sprintjabackend.service.PackageService;
import com.example.sprintjabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.sprintjabackend.constant.PackageConstant.TRACKING_NUMBER_FOUND;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final EmailService emailService;

    private final UserService userService;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository, EmailService emailService, UserService userService) {
        this.packageRepository = packageRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Override
    public Package addNewPackage(String trackingNumber, String courier, String description, double weight, double cost, UUID userId) throws TrackingNumberException, MessagingException {
        User user = userService.findUserByUserId(userId);
        validateTrackingNumber(trackingNumber);
        Package aPackage = new Package();
        aPackage.setTrackingNumber(trackingNumber);
        aPackage.setCourier(courier);
        aPackage.setDescription(description);
        aPackage.setWeight(weight);
        aPackage.setCost(cost);
        aPackage.setUserId(userId);
        aPackage.setStatus(PackageStatus.NOT_SHIPPED.toString());
        packageRepository.save(aPackage);
        emailService.sendNewPackageEmail(user.getFirstName(), user.getLastName(), user.getTrn(), trackingNumber, courier, description, weight, cost);
        return aPackage;
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

    @Override
    public Long countByStatus(String status) {
        return packageRepository.countByStatus(status.toString());
    }

    public Package getFinalCount() {
        Package aPackage = new Package();

        aPackage.setTotalPackagesNotShipped(packageRepository.countByStatus(PackageStatus.NOT_SHIPPED.toString()));
        aPackage.setTotalPackagesShipped(packageRepository.countByStatus(PackageStatus.SHIPPED.toString()));
        aPackage.setTotalPackagesReadyForPickUp(packageRepository.countByStatus(PackageStatus.READY_FOR_PICKUP.toString()));

        return aPackage;
    }


    @Override
    public List<Package> findAllByUserIdAndStatusOrderByCreatedAtDesc(UUID uuid, String Status) {
        return packageRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(uuid, PackageStatus.NOT_SHIPPED.toString());
    }


    @Override
    public Package updatePackage(String oldTrackingNumber, String trackingNumber, String courier,
                                 String description, double weight, double cost, UUID userId) throws TrackingNumberException {

        Package getPackageToBeUpdated = new Package();
        getPackageToBeUpdated = packageRepository.findByTrackingNumber(oldTrackingNumber);
        validateTrackingNumber(trackingNumber);
        getPackageToBeUpdated.setTrackingNumber(trackingNumber);
        getPackageToBeUpdated.setCourier(courier);
        getPackageToBeUpdated.setDescription(description);
        getPackageToBeUpdated.setWeight(weight);
        getPackageToBeUpdated.setCost(cost);
        getPackageToBeUpdated.setUserId(userId);
        getPackageToBeUpdated.setUpdatedAt(new Date());
        return packageRepository.save(getPackageToBeUpdated);
    }


    private void validateTrackingNumber(String trackingNumber) throws TrackingNumberException {

        Package findByTrackingNumber = packageRepository.findByTrackingNumber(trackingNumber);

        if (findByTrackingNumber != null) {
            throw new TrackingNumberException(TRACKING_NUMBER_FOUND);
        }
    }
}

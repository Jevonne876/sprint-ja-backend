package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.exception.domain.TrackingNumberException;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.repository.PackageRepository;
import com.example.sprintjabackend.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.sprintjabackend.constant.PackageConstant.TRACKING_NUMBER_FOUND;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public Package addNewPackage(String trackingNumber, String description, double weight, double cost, UUID userId) throws TrackingNumberException {
        validateTrackingNumber(trackingNumber);
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

    @Override
    public Package updatePackage(String oldTrackingNumber, String trackingNumber,
                                 String description, double weight, double cost, UUID userId) throws TrackingNumberException {

        Package getPackageToBeUpdated = new Package();
        getPackageToBeUpdated = packageRepository.findByTrackingNumber(oldTrackingNumber);
        validateTrackingNumber(trackingNumber);
        getPackageToBeUpdated.setTrackingNumber(trackingNumber);
        getPackageToBeUpdated.setDescription(description);
        getPackageToBeUpdated.setWeight(weight);
        getPackageToBeUpdated.setCost(cost);
        getPackageToBeUpdated.setUserId(userId);
        return packageRepository.save(getPackageToBeUpdated);
    }

    private void validateTrackingNumber(String trackingNumber) throws TrackingNumberException {

        Package findByTrackingNumber = packageRepository.findByTrackingNumber(trackingNumber);

        if (findByTrackingNumber != null) {
            throw new TrackingNumberException(TRACKING_NUMBER_FOUND);
        }
    }
}

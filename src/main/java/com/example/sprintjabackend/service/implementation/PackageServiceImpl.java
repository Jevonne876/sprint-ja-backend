package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.enums.PackageStatus;
import com.example.sprintjabackend.exception.domain.TrackingNumberException;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.model.UserPackageInfo;
import com.example.sprintjabackend.repository.PackageRepository;
import com.example.sprintjabackend.service.PackageService;
import com.example.sprintjabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.sprintjabackend.constant.PackageConstant.TRACKING_NUMBER_FOUND;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.Files.copy;


@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final EmailService emailService;

    private final UserService userService;

    public static final String DIRECTORY = "invoices";

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository, EmailService emailService, UserService userService) {
        this.packageRepository = packageRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Override
    public Package addNewPackage(String trackingNumber, String courier, String description, double weight, double cost, UUID userId, MultipartFile file) throws TrackingNumberException, MessagingException, IOException {
        User user = userService.findUserByUserId(userId);
        //saves the file first in order to get filename
        validateTrackingNumber(trackingNumber);
        String filename = fileUpload(trackingNumber, file);
        Package aPackage = new Package();
        aPackage.setTrackingNumber(trackingNumber);
        aPackage.setCourier(courier);
        aPackage.setDescription(description);
        aPackage.setWeight(weight);
        aPackage.setCost(cost);
        aPackage.setUserId(userId);
        aPackage.setInvoice(filename);
        aPackage.setStatus(PackageStatus.NOT_SHIPPED.toString());
        packageRepository.save(aPackage);
        emailService.sendNewPackageEmail(user.getFirstName(), user.getLastName(), user.getTrn(), trackingNumber, courier, description, weight, cost);
        return aPackage;
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

    @Override
    public Page<Package> findAll(Pageable pageable) {

        return packageRepository.findAll(pageable);
    }

    @Override
    public Page<Package> findAllNotShipped(Pageable pageable) {

        return packageRepository.findAllByStatus(pageable,PackageStatus.NOT_SHIPPED.toString());
    }

    @Override
    public Page<Package> findAllShipped(Pageable pageable) {

        return packageRepository.findAllByStatus(pageable,PackageStatus.SHIPPED.toString());
    }

    @Override
    public Page<Package> findAllReadyForPickup(Pageable pageable) {

        return packageRepository.findAllByStatus(pageable,PackageStatus.READY_FOR_PICKUP.name().toString());
    }

    @Override
    public Page<Package> findAllDelivered(Pageable pageable) {

        return packageRepository.findAllByStatus(pageable,PackageStatus.DELIVERED.name().toString());
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
    public Long countByUserIdAndStatus(UUID userId, String status) {
        return packageRepository.countByUserIdAndStatus(userId, status);
    }

    public UserPackageInfo getFinalCount(UUID userId) {
        UserPackageInfo userPackageInfo = new UserPackageInfo();
        userPackageInfo.setTotalPackagesNotShipped(userPackagesNotShipped(userId));
        userPackageInfo.setTotalPackagesShipped(userPackagesShipped(userId));
        userPackageInfo.setTotalPackagesReadyForPickup(userPackagesReadyForPickup(userId));
        userPackageInfo.setTotalPackagesDelivered(userPackagesDelivered(userId));
        return userPackageInfo;
    }

    @Override
    public List<Package> findAllByUserIdAndStatusOrderByCreatedAtDesc(UUID uuid, String Status) {
        return packageRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(uuid, PackageStatus.NOT_SHIPPED.toString());
    }

    @Override
    public Long packagesNotShipped() {
        return packageRepository.countByStatus(PackageStatus.NOT_SHIPPED.toString());
    }

    @Override
    public Long packagesShipped() {
        return packageRepository.countByStatus(PackageStatus.SHIPPED.toString());
    }

    @Override
    public Long packagesReadyForPickup() {
        return packageRepository.countByStatus(PackageStatus.READY_FOR_PICKUP.toString());
    }

    @Override
    public Long packagesDelivered() {
        return packageRepository.countByStatus(PackageStatus.DELIVERED.toString());
    }

    @Override
    public String fileUpload(String packageTrackingNumber, MultipartFile multipartFile) throws IOException {

        String newFileName;
        newFileName = packageTrackingNumber + "-" + StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Path fileStorage = get(DIRECTORY, newFileName).toAbsolutePath().normalize();
        copy(multipartFile.getInputStream(), fileStorage, REPLACE_EXISTING);
        return newFileName;
    }

    //functions below are related to user admin
    @Override
    public Long userPackagesNotShipped(UUID userId) {
        return packageRepository.countByUserIdAndStatus(userId, PackageStatus.NOT_SHIPPED.toString());
    }

    @Override
    public Long userPackagesShipped(UUID userId) {
        return packageRepository.countByUserIdAndStatus(userId, PackageStatus.SHIPPED.toString());
    }

    @Override
    public Long userPackagesReadyForPickup(UUID userId) {
        return packageRepository.countByUserIdAndStatus(userId, PackageStatus.READY_FOR_PICKUP.toString());
    }

    @Override
    public Long userPackagesDelivered(UUID userId) {
        return packageRepository.countByUserIdAndStatus(userId, PackageStatus.DELIVERED.toString());
    }

    //functions below are related to user
    @Override
    public Page<Package> findAllUserPackagesNotShipped(UUID userId, Pageable pageable) {
        return packageRepository.findAllByUserIdAndStatusOrderByUpdatedAtDesc(userId, PackageStatus.NOT_SHIPPED.toString(), pageable);
    }

    @Override
    public Page<Package> findAllUserPackagesShipped(UUID userId, Pageable pageable) {
        return packageRepository.findAllByUserIdAndStatusOrderByUpdatedAtDesc(userId, PackageStatus.SHIPPED.toString(), pageable);
    }

    @Override
    public Page<Package> findAllUserPackagesReadyForPickup(UUID userId, Pageable pageable) {
        return packageRepository.findAllByUserIdAndStatusOrderByUpdatedAtDesc(userId, PackageStatus.READY_FOR_PICKUP.toString(), pageable);
    }

    @Override
    public Page<Package> findAllUserPackagesDelivered(UUID userId, Pageable pageable) {
        return packageRepository.findAllByUserIdAndStatusOrderByUpdatedAtDesc(userId, PackageStatus.DELIVERED.toString(), pageable);
    }

    private void validateTrackingNumber(String trackingNumber) throws TrackingNumberException {

        Package findByTrackingNumber = packageRepository.findByTrackingNumber(trackingNumber);

        if (findByTrackingNumber != null) {
            throw new TrackingNumberException(TRACKING_NUMBER_FOUND);
        }
    }
}

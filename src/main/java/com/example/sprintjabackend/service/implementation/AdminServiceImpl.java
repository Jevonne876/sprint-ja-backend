package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.enums.Role;
import com.example.sprintjabackend.exception.domain.UsernameExistException;
import com.example.sprintjabackend.model.ApplicationInfo;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.repository.AdminRepository;
import com.example.sprintjabackend.repository.PackageRepository;
import com.example.sprintjabackend.repository.UserRepository;
import com.example.sprintjabackend.service.AdminService;
import com.example.sprintjabackend.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final PackageService packageService;
    private final PackageRepository packageRepository;

    private final EmailService emailService;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, BCryptPasswordEncoder passwordEncoder,
                            UserRepository userRepository, PackageService packageService,
                            PackageRepository packageRepository, EmailService emailService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.packageService = packageService;
        this.packageRepository = packageRepository;
        this.emailService = emailService;
    }

    @Override
    public User registerNewAdmin(String fistName, String lastName, String username, String password) throws UsernameExistException {
        User newAdminUser = new User();
        validateUsername(username);
        newAdminUser.setTrn(0L);
        newAdminUser.setFirstName(fistName);
        newAdminUser.setLastName(lastName);
        newAdminUser.setUsername(username);
        newAdminUser.setEmail(username);
        newAdminUser.setPassword(passwordEncoder.encode(password));
        newAdminUser.setRole(Role.ROLE_SUPER_ADMIN.name());
        newAdminUser.setAuthorities(Role.ROLE_SUPER_ADMIN.getAuthorities());
        adminRepository.save(newAdminUser);
        return newAdminUser;
    }

    @Override
    public User updateAdmin(String fistName, String lastName, String username, String password) {
        return null;
    }

    @Override
    public User findAdminByUsername(String username) {
        return userRepository.findUserByUsername(username);

    }

    @Override
    public User findAdminByUserId(UUID userId) {
        return userRepository.findUserByUserId(userId);
    }


    @Override
    public ApplicationInfo getData() {

        ApplicationInfo applicationInfo = new ApplicationInfo();

        applicationInfo.setTotalUsers(countByRoleUser());
        applicationInfo.setTotalAdmins(countByRoleAdmin());

        applicationInfo.setTotalPackagesNotShipped(packageService.packagesNotShipped());
        applicationInfo.setTotalPackagesShipped(packageService.packagesShipped());
        applicationInfo.setTotalPackagesReadyForPickup(packageService.packagesReadyForPickup());
        applicationInfo.setTotalPackagesDelivered(packageService.packagesDelivered());
        applicationInfo.setTotalPackages(packageService.packagesNotShipped() + packageService.packagesShipped() +
                packageService.packagesReadyForPickup() + packageService.packagesDelivered());

        return applicationInfo;
    }

    @Override
    public long countByRoleUser() {
        return adminRepository.countByRole(Role.ROLE_USER.toString());
    }

    @Override
    public long countByRoleAdmin() {
        return adminRepository.countByRole(Role.ROLE_SUPER_ADMIN.toString());
    }


    @Override
    public List<String> findAllEmails() {
        return adminRepository.findAllByRole(Role.ROLE_USER.toString());
    }



    private void validateUsername(String username) throws UsernameExistException {
        User adminUser = findAdminByUsername(username);
        if (adminUser != null) {
            throw new UsernameExistException("This username is already taken");
        }
    }
}

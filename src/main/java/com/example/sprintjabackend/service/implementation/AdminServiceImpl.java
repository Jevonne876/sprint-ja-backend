package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.enums.Role;
import com.example.sprintjabackend.exception.domain.UsernameExistException;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.repository.AdminRepository;
import com.example.sprintjabackend.repository.UserRepository;
import com.example.sprintjabackend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, BCryptPasswordEncoder passwordEncoder,
                            UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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


    private void validateUsername(String username) throws UsernameExistException {
        User adminUser = findAdminByUsername(username);
        if (adminUser != null) {
            throw new UsernameExistException("This username is already taken");
        }
    }
}

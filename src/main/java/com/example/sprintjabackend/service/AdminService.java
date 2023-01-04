package com.example.sprintjabackend.service;

import com.example.sprintjabackend.enums.Role;
import com.example.sprintjabackend.exception.domain.UsernameExistException;
import com.example.sprintjabackend.model.ApplicationInfo;
import com.example.sprintjabackend.model.User;

import java.util.UUID;

public interface AdminService {

    User registerNewAdmin(String fistName, String lastName, String username, String password) throws UsernameExistException;

    User updateAdmin(String fistName, String lastName, String username, String password);

    User findAdminByUsername(String username);

    User findAdminByUserId(UUID userId);

    long countByRoleUser();
    long countByRoleAdmin();
//    long totalPackages();
    ApplicationInfo getData();
}

package com.example.sprintjabackend.repository;

import com.example.sprintjabackend.enums.Role;
import com.example.sprintjabackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface AdminRepository extends CrudRepository<User, Long> {

    //returns all admin
    Page<User> findAllByRole(Pageable pageable, Role role);

    //returns all admin
    List<User> findAllByRole(Role role);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserByTrn(Long trn);

    User findUserByPhoneNumber(String phoneNumber);

    User findUserByUserId(UUID userId);

    long countByRole(String role);

}

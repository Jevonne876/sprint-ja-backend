package com.example.sprintjabackend.repository;

import com.example.sprintjabackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserByTrn(Long trn);

    User findUserByPhoneNumber(String phoneNumber);

    User findUserByUserId(UUID userId);

    Page<User> findAllByRoleOrderByUpdatedAtDesc(Pageable pageable, String role);

    List<User> findAllByRoleOrderByLastNameAsc(String role);

    Page<User> findAllByRoleAndFirstNameContainingIgnoreCaseOrderByLastNameAsc(Pageable pageable, String role, String firstName);
}

package com.example.sprintjabackend.repository;

import com.example.sprintjabackend.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, Long> {

    Page <Package> findAll(Pageable pageable);

    Page <Package> findAllByUserId(Long id, Pageable pageable);


}

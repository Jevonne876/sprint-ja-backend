package com.example.sprintjabackend.repository;

import com.example.sprintjabackend.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Long> {

    Page <Package> findAll(Pageable pageable);


}

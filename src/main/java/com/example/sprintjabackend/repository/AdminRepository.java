package com.example.sprintjabackend.repository;

import com.example.sprintjabackend.model.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Long> {

    Admin findByUsername(String username);


}

package com.example.sprintjabackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

import static com.example.sprintjabackend.enums.Role.SUPER_ADMIN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin")
@Entity
@ToString
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id", nullable = false, updatable = false, unique = true)
    private UUID adminId = UUID.randomUUID();

    @Column(name = "username",nullable = false,unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    String password;

    private String role = SUPER_ADMIN.name();
    private String[] authorities = SUPER_ADMIN.getAuthorities();

    private boolean isActive = true;
    private boolean isNotLocked = true;

    @Column(name = "updatedAt", nullable = false)
    Date updatedAt = new Date();

    @Column(name = "createdAt", nullable = false)
    Date createdAt = new Date();

    public Admin(UUID adminId, String username, String password,
                 boolean isActive, boolean isNotLocked, Date updatedAt, Date createdAt) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.role = SUPER_ADMIN.name();
        this.authorities = SUPER_ADMIN.getAuthorities();
        this.isActive = true;
        this.isNotLocked = true;
        this.updatedAt = new Date();
        this.createdAt = new Date();
    }



    @JsonIgnore
    @JsonProperty(value = "password")
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @JsonProperty(value = "id")
    public Long getId() {
        return id;
    }
}

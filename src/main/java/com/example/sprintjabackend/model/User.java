package com.example.sprintjabackend.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import static com.example.sprintjabackend.enums.Role.ROLE_USER;


@Table(name = "users")
@Entity
@ToString
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private UUID userId = UUID.randomUUID();

    @Column(name = "trn")
    Long trn;

    @Column(name = "firstName")
    String firstName;

    @Column(name = "lastName")
    String lastName;

    @Column(name = "dateOfBirth")
    Date dateOfBirth;

    @Column(name = "username")
    String username;
    @Column(name = "email")
    String email;
    @Column(name = "password")
    String password;
    @Column(name = "phone")
    String phoneNumber;

    @Column(name = "streetAddress")
    String streetAddress;

    @Column(name = "parish")
    String parish;

    @Column(name = "pickupBranch")
    String pickUpBranch;


    private String role = ROLE_USER.name();
    private String[] authorities = ROLE_USER.getAuthorities();

    private boolean isActive = true;
    private boolean isNotLocked = true;

    @Column(name = "updatedAt", nullable = false)
    Date updatedAt = new Date();

    @Column(name = "createdAt", nullable = false)
    Date createdAt = new Date();

    public User(){

    }
    public User(UUID userId, Long trn, String firstName, String lastName,
                Date dateOfBirth, String username, String email, String password,
                String phoneNumber, String streetAddress, String parish, String pickUpBranch,
                String role, String[] authorities, boolean isActive, boolean isNotLocked,
                Date updatedAt, Date createdAt) {
        this.userId = userId;
        this.trn = trn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.streetAddress = streetAddress;
        this.parish = parish;
        this.pickUpBranch = pickUpBranch;
        this.role = role;
        this.authorities = authorities;
        this.isActive = isActive;
        this.isNotLocked = isNotLocked;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public User(Long trn, String firstName, String lastName, Date dateOfBirth,
                String email, String password, String phoneNumber,
                String streetAddress, String parish, String pickUpBranch) {
        this.userId = UUID.randomUUID();
        this.trn = trn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.username = email;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.streetAddress = streetAddress;
        this.parish = parish;
        this.pickUpBranch = pickUpBranch;
        this.role = ROLE_USER.name();
        this.authorities = ROLE_USER.getAuthorities();
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Long getTrn() {
        return trn;
    }

    public void setTrn(Long trn) {
        this.trn = trn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public String getPickUpBranch() {
        return pickUpBranch;
    }

    public void setPickUpBranch(String pickUpBranch) {
        this.pickUpBranch = pickUpBranch;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNotLocked() {
        return isNotLocked;
    }

    public void setNotLocked(boolean notLocked) {
        isNotLocked = notLocked;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

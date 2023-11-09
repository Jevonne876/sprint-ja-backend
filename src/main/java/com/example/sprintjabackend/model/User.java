package com.example.sprintjabackend.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import static com.example.sprintjabackend.enums.Role.ROLE_USER;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


}

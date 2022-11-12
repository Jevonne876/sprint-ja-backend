package com.example.sprintjabackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true, insertable = false)
    private Long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false, updatable = false, unique = true, insertable = false)
    private UUID userId;

    @Column(name = "trn", nullable = false, unique = true)
    Long trn;

    @Column(name = "firstName", nullable = false)
    String firstName;

    @Column(name = "lastName", nullable = false)
    String lastName;

    @Column(name = "dateOfBirth", nullable = false)
    Date dateOfBirth;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "phone", nullable = false, unique = true)
    String phoneNumber;

    @Column(name = "address1", nullable = false)
    String address1;

    @Column(name = "address2", nullable = true)
    String address2;

    @Column(name = "pickupBranch", nullable = false, updatable = true)
    String pickUpBranch;

    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;

    @Column(name = "updatedAt", nullable = false)
    Date updatedAt = new Date();

    @Column(name = "createdAt", nullable = false)
    Date createdAt = new Date();


    public Users(UUID userId, Long trn, String firstName, String lastName, Date dateOfBirth, String email,
                 String password, String phoneNumber, String address1, String address2, String pickUpBranch,
                 String role, String[] authorities) {

        this.userId = userId;
        this.trn = trn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address1 = address1;
        this.address2 = address2;
        this.pickUpBranch = pickUpBranch;
        this.role = role;
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", trn=" + trn +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", pickUpBranch='" + pickUpBranch + '\'' +
                ", role='" + role + '\'' +
                ", authorities=" + Arrays.toString(authorities) +
                '}';
    }
}

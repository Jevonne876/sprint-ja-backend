package com.example.sprintjabackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.sprintjabackend.enums.Role.ROLE_USER;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private UUID userId = UUID.randomUUID();

    @Column(name = "trn", nullable = false, unique = true)
    Long trn;

    @Column(name = "firstName", nullable = false)
    String firstName;

    @Column(name = "lastName", nullable = false)
    String lastName;

    @Column(name = "dateOfBirth", nullable = false)
    Date dateOfBirth;

    @Column(name = "username", nullable = false)
    String username;
    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "phone", nullable = false, unique = true)
    String phoneNumber;

    @Column(name = "address1", nullable = false, updatable = true)
    String address1;

    @Column(name = "address2")
    String address2;

    @Column(name = "pickupBranch", updatable = true)
    String pickUpBranch;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Package> packages;

    private String role = ROLE_USER.name();
    private String[] authorities = ROLE_USER.getAuthorities();

    private boolean isActive = true;
    private boolean isNotLocked = true;

    @Column(name = "updatedAt", nullable = false)
    Date updatedAt = new Date();

    @Column(name = "createdAt", nullable = false)
    Date createdAt = new Date();


    public User(Long id, UUID userId, Long trn, String firstName,
                String lastName, Date dateOfBirth, String username, String email,
                String password, String phoneNumber, String address1, String address2,
                String pickUpBranch, String role, String[] authorities, boolean isActive,
                boolean isNotLocked, Date updatedAt, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.trn = trn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address1 = address1;
        this.address2 = address2;
        this.pickUpBranch = pickUpBranch;
        this.role = role;
        this.authorities = authorities;
        this.isActive = isActive;
        this.isNotLocked = isNotLocked;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public User(UUID userId, Long trn, String firstName, String lastName,
                Date dateOfBirth, String username, String email, String password, String
                        phoneNumber, String address1, String address2, String pickUpBranch,
                String role, String[] authorities, boolean isActive, boolean isNotLocked, Date updatedAt, Date createdAt) {
        this.userId = userId;
        this.trn = trn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address1 = address1;
        this.address2 = address2;
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
                String address1, String address2, String pickUpBranch) {
        this.userId = UUID.randomUUID();
        this.trn = trn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.username = email;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address1 = address1;
        this.address2 = address2;
        this.pickUpBranch = pickUpBranch;
        this.role = ROLE_USER.name();
        this.authorities = ROLE_USER.getAuthorities();
        this.isActive = true;
        this.isNotLocked = true;
        this.updatedAt = new Date();
        this.createdAt = new Date();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId=" + userId +
                ", trn=" + trn +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", pickUpBranch='" + pickUpBranch + '\'' +
                ", role='" + role + '\'' +
                ", authorities=" + Arrays.toString(authorities) +
                ", isActive=" + isActive +
                ", isNotLocked=" + isNotLocked +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}

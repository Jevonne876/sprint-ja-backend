package com.example.sprintjabackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Table
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
    @Column(name = "package_id", nullable = false, updatable = false, unique = true)
    private UUID packageId = UUID.randomUUID();
    @Column(name = "trackingNumber", unique = true, nullable = false)
    private String trackingNumber;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "cost", nullable = false)
    private double cost;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn( nullable = false)
    private User user;

    @Column(name = "updatedAt", nullable = false)
    Date updatedAt = new Date();

    @Column(name = "createdAt", nullable = false)
    Date createdAt = new Date();


    public Package(UUID packageId, String trackingNumber, String description, double weight, double cost, Date updatedAt, Date createdAt) {
        this.packageId = packageId;
        this.trackingNumber = trackingNumber;
        this.description = description;
        this.weight = weight;
        this.cost = cost;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public Package(String trackingNumber, String description, double weight, double cost, User user) {
        this.trackingNumber = trackingNumber;
        this.description = description;
        this.weight = weight;
        this.cost = cost;
        this.user = user;
    }

    //getter method to retrieve the AuthorId
    public UUID getUserId(){
        return user.getUserId();
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Package{" +
                "id=" + id +
                ", packageId=" + packageId +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", description='" + description + '\'' +
                ", weight=" + weight +
                ", cost=" + cost +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}

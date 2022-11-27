package com.example.sprintjabackend.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Table
@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "p_id")
public class Package implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long p_id;
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
        this.updatedAt = new Date();
        this.createdAt = new Date();
    }

    public Package( String description, double weight, double cost) {
        this.trackingNumber = trackingNumber;
        this.description = description;
        this.weight = weight;
        this.cost = cost;
        this.updatedAt = new Date();
    }
}

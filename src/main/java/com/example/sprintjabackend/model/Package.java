package com.example.sprintjabackend.model;

import com.example.sprintjabackend.enums.PackageStatus;
import com.fasterxml.jackson.annotation.*;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Table
@Entity

public class Package implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
    @Column(name = "package_id", nullable = false, updatable = false, unique = true)
    private UUID packageId = UUID.randomUUID();
    @Column(name = "trackingNumber", nullable = false)
    private String trackingNumber;

    @Column(name = "courier", nullable = false)
    private String courier;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "cost", nullable = false)
    private double cost;

    @Column(name = "userId", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "status")
    private String status;

    @Column(name = "firstName")
    String firstName;

    @Column(name = "lastName")
    String lastName;


    @Column(name = "totalPackagesNotShipped", nullable = true, updatable = true)
    private Long totalPackagesNotShipped;


    @Column(name = "totalPackagesShipped", nullable = true, updatable = true)
    private Long totalPackagesShipped;

    @Column(name = "totalPackagesReadyForPickup", nullable = true, updatable = true)
    private Long totalPackagesReadyForPickUp;

    @Column(name = "invoice")
    private String invoice;

    @Column(name = "updatedAt", nullable = false)
    @Temporal(TemporalType.DATE)
    Date updatedAt = new Date();

    @Column(name = "createdAt", nullable = false)
    @Temporal(TemporalType.DATE)
    Date createdAt = new Date();

    public Package() {

    }

    public Package(UUID packageId, String trackingNumber, String courier, String description,
                   double weight, double cost, UUID userId, Date updatedAt, Date createdAt, String invoice) {
        this.packageId = packageId;
        this.trackingNumber = trackingNumber;
        this.courier = courier;
        this.description = description;
        this.weight = weight;
        this.cost = cost;
        this.userId = userId;
        this.invoice = invoice;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;

    }

    public Package(String trackingNumber, String courier, String description, double weight, double cost, UUID userId, String invoiceUrl) {
        this.trackingNumber = trackingNumber;
        this.courier = courier;
        this.description = description;
        this.weight = weight;
        this.cost = cost;
        this.userId = userId;
        this.status = PackageStatus.DEFAULT.toString();
        this.invoice = invoice;
        this.updatedAt = new Date();
        this.createdAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPackageId() {
        return packageId;
    }

    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getTotalPackagesNotShipped() {
        return totalPackagesNotShipped;
    }

    public void setTotalPackagesNotShipped(Long totalPackagesNotShipped) {
        this.totalPackagesNotShipped = totalPackagesNotShipped;
    }

    public Long getTotalPackagesShipped() {
        return totalPackagesShipped;
    }

    public void setTotalPackagesShipped(Long totalPackagesShipped) {
        this.totalPackagesShipped = totalPackagesShipped;
    }

    public Long getTotalPackagesReadyForPickUp() {
        return totalPackagesReadyForPickUp;
    }

    public void setTotalPackagesReadyForPickUp(Long totalPackagesReadyForPickUp) {
        this.totalPackagesReadyForPickUp = totalPackagesReadyForPickUp;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
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

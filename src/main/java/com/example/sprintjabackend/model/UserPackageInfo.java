package com.example.sprintjabackend.model;

import lombok.*;


public class UserPackageInfo {
    private long totalPackagesNotShipped;
    private long totalPackagesShipped;
    private long totalPackagesReadyForPickup;
    private long totalPackagesDelivered;

    public UserPackageInfo(long totalPackagesNotShipped, long totalPackagesShipped, long totalPackagesReadyForPickup, long totalPackagesDelivered) {
        this.totalPackagesNotShipped = totalPackagesNotShipped;
        this.totalPackagesShipped = totalPackagesShipped;
        this.totalPackagesReadyForPickup = totalPackagesReadyForPickup;
        this.totalPackagesDelivered = totalPackagesDelivered;
    }

    public UserPackageInfo() {

    }

    public long getTotalPackagesNotShipped() {
        return totalPackagesNotShipped;
    }

    public void setTotalPackagesNotShipped(long totalPackagesNotShipped) {
        this.totalPackagesNotShipped = totalPackagesNotShipped;
    }

    public long getTotalPackagesShipped() {
        return totalPackagesShipped;
    }

    public void setTotalPackagesShipped(long totalPackagesShipped) {
        this.totalPackagesShipped = totalPackagesShipped;
    }

    public long getTotalPackagesReadyForPickup() {
        return totalPackagesReadyForPickup;
    }

    public void setTotalPackagesReadyForPickup(long totalPackagesReadyForPickup) {
        this.totalPackagesReadyForPickup = totalPackagesReadyForPickup;
    }

    public long getTotalPackagesDelivered() {
        return totalPackagesDelivered;
    }

    public void setTotalPackagesDelivered(long totalPackagesDelivered) {
        this.totalPackagesDelivered = totalPackagesDelivered;
    }
}

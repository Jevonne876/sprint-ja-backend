package com.example.sprintjabackend.model;

import lombok.*;


public class ApplicationInfo {
    private long totalUsers;
    private long totalAdmins;
    private long totalPackages;
    private long totalPackagesNotShipped;
    private long totalPackagesShipped;
    private long totalPackagesReadyForPickup;
    private long totalPackagesDelivered;

    public ApplicationInfo() {
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalAdmins() {
        return totalAdmins;
    }

    public void setTotalAdmins(long totalAdmins) {
        this.totalAdmins = totalAdmins;
    }

    public long getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(long totalPackages) {
        this.totalPackages = totalPackages;
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

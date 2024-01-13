package com.example.sprintjabackend.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApplicationInfo {
    private long totalUsers;
    private long totalAdmins;
    private long totalPackages;
    private long totalPackagesNotShipped;
    private long totalPackagesShipped;
    private long totalPackagesReadyForPickup;
    private long totalPackagesDelivered;
}

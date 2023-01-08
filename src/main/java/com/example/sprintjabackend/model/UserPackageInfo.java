package com.example.sprintjabackend.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserPackageInfo {
    private long totalPackagesNotShipped;
    private long totalPackagesShipped;
    private long totalPackagesReadyForPickup;
    private long totalPackagesDelivered;
}

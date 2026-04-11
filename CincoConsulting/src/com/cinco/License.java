package com.cinco;

import java.util.UUID;

/**
 * Represents a software or service license, extending the item class
 */
public class License extends Item {
    private double serviceFee;
    private double annualFee;

    public License(UUID uuid, String name, double serviceFee, double annualFee) {
        super(uuid, name);
        this.serviceFee = serviceFee;
        this.annualFee = annualFee;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public double getAnnualFee() {
        return annualFee;
    }
}
package com.krukovska.paymentsystem.persistence.model;


public enum PaymentStatus {

    CREATED("Created"),
    SENT("Sent");


    private final String name;

    PaymentStatus(String name) {
        this.name = name;
    }
}

package com.krukovska.paymentsystem.persistence.model;



public enum ClientStatus {

    BLOCKED("Blocked"),
    ACTIVE("Active");

    ClientStatus(String name) {
        this.name = name;
    }
    private final String name;
}

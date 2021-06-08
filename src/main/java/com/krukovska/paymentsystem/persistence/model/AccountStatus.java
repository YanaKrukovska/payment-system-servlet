package com.krukovska.paymentsystem.persistence.model;

public enum AccountStatus {

    BLOCKED("Blocked"),
    ACTIVE("Active");


    AccountStatus(String name) {
        this.name = name;
    }

    private final String name;

}
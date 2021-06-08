package com.krukovska.paymentsystem.persistence.model;

import java.time.LocalDate;

public class UnblockRequest extends Entity {

    private Client client;
    private Account account;
    private LocalDate creationDate;
    private LocalDate actionDate;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    @Override
    public String toString() {
        return "UnblockRequest: {client=" + client + ", account = " + account +
                ", creationDate = " + creationDate + ", actionDate = " + actionDate + '}';
    }
}

package com.krukovska.paymentsystem.persistence.model;

import java.util.List;

public class Client extends Entity {

    private User user;
    private ClientStatus status;
    private List<Account> accounts;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Client: { user = " + user + ", status = " + status +
                ", accounts = " + accounts + '}';
    }
}

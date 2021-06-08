package com.krukovska.paymentsystem.persistence.model;

import java.math.BigDecimal;

public class Account extends Entity {

    private Client client;
    private String name;
    private BigDecimal balance;
    private AccountStatus status;
    private String iban;
    private CreditCard creditCard;


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public String toString() {
        return "Account: {client = " + client + ", name = '" + name +
                ", balance = " + balance + ", status = " + status + ", iban = '" + iban +
                ", creditCard = " + creditCard + '}';
    }
}


package com.krukovska.paymentsystem.persistence.model;

public class CreditCard  extends Entity{

    private Account account;
    private String cardNumber;
    private boolean isExpired;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    @Override
    public String toString() {
        return "CreditCard: { account = " + account + ", cardNumber = '" + cardNumber +
                ", isExpired = " + isExpired + '}';
    }
}

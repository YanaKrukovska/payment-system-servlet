package com.krukovska.paymentsystem.persistence.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment extends Entity {

    private Account account;
    private BigDecimal amount;
    private PaymentStatus status;
    private String receiverIban;
    private String details;
    private LocalDate paymentDate;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getReceiverIban() {
        return receiverIban;
    }

    public void setReceiverIban(String receiverIban) {
        this.receiverIban = receiverIban;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment: {account = " + account + ", amount = " + amount + ", status = " + status +
                ", receiverIban = '" + receiverIban + ", details = '" + details +
                ", paymentDate = " + paymentDate + '}';
    }
}

package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.Response;

import java.util.List;

public interface PaymentService {

    /**
     * find all payments of certain client
     *
     * @param clientId id of client whose payments need to be found
     * @param page     paging and sorting parameters for search
     * @return found payments
     */
    List<Payment> findAllClientPayments(long clientId, PageAndSort page);

    /**
     * saves a new payment
     *
     * @param payment payment that needs to be saved
     * @return created payment
     */
    Payment create(Payment payment);

    /**
     * sends a payment: changes it's status and withdraws money from account's balance
     *
     * @param paymentId id of payment that needs to be sent
     * @return sent payment and list of error messages
     */
    Response<Payment> send(long paymentId);

    /**
     * counts amount of client's payments
     *
     * @param clientId id of client whose payments need to be counted
     * @return amount of client's payments
     */
    long count(long clientId);
}

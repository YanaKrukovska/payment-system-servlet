package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.Response;

import java.util.List;

public interface PaymentService {
    List<Payment> findAllClientPayments(long clientId, PageAndSort page);

    Payment create(Payment payment);

    Response<Payment> send(long paymentId);

    long count(long clientId);
}

package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.Response;

import java.sql.SQLException;
import java.util.List;

public interface PaymentRepository extends Repository<Payment> {
    List<Payment> findClientPayments(long clientId, PageAndSort page) throws SQLException;

    Response<Payment> send(long paymentId) throws SQLException;

    long countByClient(long clientId) throws SQLException;
}

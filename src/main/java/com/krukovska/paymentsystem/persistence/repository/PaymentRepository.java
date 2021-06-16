package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.Response;

import java.sql.SQLException;
import java.util.List;

public interface PaymentRepository extends Repository<Payment> {
    /**
     * finds all payments of certain client
     *
     * @param clientId id of client whose payments need to be found
     * @param page     paging and sorting parameters for search
     * @return list of client's payments
     * @throws SQLException in case of db issues
     */
    List<Payment> findClientPayments(long clientId, PageAndSort page) throws SQLException;

    /**
     * sends payment, updates status to sent and withdraws money from account the payment belongs to
     *
     * @param paymentId id of payment that is being sent
     * @return updated payment and list of error messages
     * @throws SQLException in case of db issues
     */
    Response<Payment> send(long paymentId) throws SQLException;

    /**
     * counts all payments of certain client
     *
     * @param clientId id of clients whose payments are counted
     * @return amount of client's payments
     * @throws SQLException in case of db issues
     */
    long countByClient(long clientId) throws SQLException;
}

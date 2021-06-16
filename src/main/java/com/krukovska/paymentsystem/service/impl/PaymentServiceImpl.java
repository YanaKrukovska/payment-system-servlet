package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.PaymentRepository;
import com.krukovska.paymentsystem.service.PaymentService;
import com.krukovska.paymentsystem.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository repository;

    public PaymentServiceImpl(PaymentRepository repository) {
        this.repository = repository;
    }

    /**
     * find all payments of certain client
     *
     * @param clientId id of client whose payments need to be found
     * @param page     paging and sorting parameters for search
     * @return found payments
     */
    @Override
    public List<Payment> findAllClientPayments(long clientId, PageAndSort page) {
        logger.debug("Find payments for clientId {}, page and sort {}", clientId, page);
        requireNonNull(page, "page must be not null");

        try {
            return repository.findClientPayments(clientId, page);
        } catch (SQLException e) {
            String errorMessage = "Error during getting payments for client with id=" + clientId;
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    /**
     * saves a new payment
     *
     * @param payment payment that needs to be saved
     * @return created payment
     */
    @Override
    public Payment create(Payment payment) {
        logger.debug("Creating new payment {}", payment);
        requireNonNull(payment, "payment must be not null");

        try {
            return repository.create(payment);
        } catch (SQLException e) {
            String errorMessage = "Error during creating  new payment =" + payment;
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    /**
     * sends a payment: changes it's status and withdraws money from account's balance
     *
     * @param paymentId id of payment that needs to be sent
     * @return sent payment and list of error messages
     */
    @Override
    public Response<Payment> send(long paymentId) {
        logger.debug("Sending  payment {}", paymentId);
        try {
            return repository.send(paymentId);
        } catch (SQLException e) {
            String errorMessage = "Error during sending  payment with id =" + paymentId;
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    /**
     * counts amount of client's payments
     *
     * @param clientId id of client whose payments need to be counted
     * @return amount of client's payments
     */
    @Override
    public long count(long clientId) {
        logger.debug("Count  payments for client {}", clientId);
        try {
            return repository.countByClient(clientId);
        } catch (SQLException e) {
            String errorMessage = "Error during sending  payment with id =" + clientId;
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }
}

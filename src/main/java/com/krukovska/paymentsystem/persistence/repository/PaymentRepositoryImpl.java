package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.SQLHelper;
import com.krukovska.paymentsystem.persistence.mapper.AccountMapper;
import com.krukovska.paymentsystem.persistence.mapper.Mapper;
import com.krukovska.paymentsystem.persistence.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


import static com.krukovska.paymentsystem.persistence.SQLHelper.createSelectPaymentCountForClient;
import static java.util.Collections.singletonList;


public class PaymentRepositoryImpl extends AbstractRepository<Payment> implements PaymentRepository {
    private static final Logger logger = LogManager.getLogger(PaymentRepositoryImpl.class);
    private final DataSource ds;

    private final AccountRepository accountRepository = new AccountRepositoryImpl(DataSource.getInstance(), new AccountMapper());

    public PaymentRepositoryImpl(DataSource ds, Mapper<Payment> mapper) {
        super(ds, mapper);
        this.ds = ds;
    }


    @Override
    public List<Payment> findClientPayments(long clientId, PageAndSort page) throws SQLException {
        Objects.requireNonNull(page, "Page must be not null ");
        getLogger().debug("Finding payments by client id ={},  page(number/size) :{}", clientId, page);

        return queryForObjects(SQLHelper.createPageableSelectForPayments(getTableName(), page), singletonList(clientId));
    }

    @Override
    public Response<Payment> send(long paymentId) {
        HashSet<Field> fieldsToUpdate = new HashSet<>(Arrays.asList(new Field("status",
                PaymentStatus.SENT), new Field("payment_date", LocalDate.now())));

        Response<Payment> resultResponse = new Response<>();
        try (Connection c = ds.getConnection()) {

            c.setAutoCommit(false);

            try {
                Payment payment = findById(paymentId, c);
                List<String> resultErrors = resultResponse.getErrors();

                if (payment == null) {
                    resultErrors.add("Payment with id " + paymentId + " does not exist");
                } else if (payment.getStatus() == PaymentStatus.SENT) {
                    resultErrors.add(("Payment with id " + paymentId + " is already sent"));
                } else {
                    Response<Account> accountUpdateResult = accountRepository.withdrawAccount(payment.getAccount().getId(),
                            payment.getAmount(), c);
                    resultErrors.addAll(accountUpdateResult.getErrors());
                }

                if (resultResponse.isOkay()) {
                    Response<Payment> paymentUpdaterResult = update(paymentId, fieldsToUpdate, c);
                    resultResponse.setObject(paymentUpdaterResult.getObject());
                    resultErrors.addAll(paymentUpdaterResult.getErrors());
                }

                if (resultResponse.isOkay()) {
                    c.commit();
                } else {
                    c.rollback();
                    logErrors(resultResponse);
                }

            } catch (SQLException e) {
                c.rollback();
                logger.debug("Failed to send payment: {}", e.getMessage());
            }

        } catch (SQLException e) {
            logger.debug(e.getMessage());
        }

        return resultResponse;
    }

    private void logErrors(Response<Payment> resultResponse) {
        for (String message : resultResponse.getErrors()) {
            logger.debug(message);
        }
    }

    @Override
    public long countByClient(long clientId) throws SQLException {
        getLogger().debug("Getting payment count for client id = {}", clientId);
        return count(createSelectPaymentCountForClient(), new Field("a.client_id", clientId));
    }


    @Override
    protected String getTableName() {
        return "Payments";
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}

package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.mapper.Mapper;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static com.krukovska.paymentsystem.persistence.SQLHelper.createUpdateAmountSQL;
import static com.krukovska.paymentsystem.persistence.model.AccountStatus.BLOCKED;
import static java.util.Collections.singletonList;


public class AccountRepositoryImpl extends AbstractRepository<Account> implements AccountRepository {
    private static final Logger logger = LogManager.getLogger(AccountRepositoryImpl.class);
    private final DataSource ds;

    public AccountRepositoryImpl(DataSource ds, Mapper<Account> mapper) {
        super(ds, mapper);
        this.ds = ds;
    }

    /**
     * updates balance of certain account
     *
     * @param accountId id of account which balance needs to be updates
     * @param amount    new amount of money on balance
     * @return updated account and list of error messages
     * @throws SQLException in case of database issues
     */
    @Override
    public Response<Account> updateBalance(long accountId, BigDecimal amount) throws SQLException {
        logger.debug("update account balance with id = {} by amount {}", accountId, amount);

        try (Connection c = ds.getConnection()) {
            Account account = findById(accountId, c);
            if (account == null) {
                String message = "Account with id " + accountId + " does not exist";
                logger.debug(message);
                return new Response<>(singletonList(message));
            }
            return new Response<>(updateBalance(accountId, amount, c));
        }
    }

    /**
     * updates balance of certain account in a given connection
     *
     * @param accountId id of account which balance needs to be updates
     * @param amount    new amount of money on balance
     * @param c         connection where update will be executed
     * @return updated account and list of error messages
     * @throws SQLException in case of database issues
     */
    @Override
    public Account updateBalance(long accountId, BigDecimal amount, Connection c) throws SQLException {
        logger.debug("update account balance with id = {} by amount {}", accountId, amount);

        try (PreparedStatement statement =
                     c.prepareStatement(createUpdateAmountSQL())) {
            statement.setObject(1, amount);
            statement.setObject(2, accountId);
            statement.executeUpdate();
        }
        return findById(accountId, c);
    }

    /**
     * withdraws given amount of balance of certain account
     *
     * @param accountId id of account where money will be wirthdrawn
     * @param amount    amount that will be withdrawn
     * @return updated account and list of error messages
     * @throws SQLException in case of database issues
     */
    @Override
    public Response<Account> withdrawAccount(Long accountId, BigDecimal amount) throws SQLException {
        logger.debug("withdraw account balance with id = {} by amount {}", accountId, amount);

        try (Connection c = ds.getConnection()) {
            return withdrawAccount(accountId, amount, c);
        }
    }

    /**
     * withdraws given amount of balance of certain account in a given connection
     *
     * @param accountId id of account where money will be wirthdrawn
     * @param amount    amount that will be withdrawn
     * @param c         connection where update will be executed
     * @return updated account and list of error messages
     * @throws SQLException in case of database issues
     */
    @Override
    public Response<Account> withdrawAccount(Long accountId, BigDecimal amount, Connection c) throws SQLException {
        Objects.requireNonNull(accountId, "accountId must be not null");
        Objects.requireNonNull(amount, "amount must be not null");
        Objects.requireNonNull(c, "connection must be not null");
        logger.debug("withdraw account balance with id = {} by amount {}", accountId, amount);

        Account account = findById(accountId, c);
        if (account == null) {
            String message = "Account with id " + accountId + " does not exist";
            logger.debug(message);
            return new Response<>(singletonList(message));
        }

        if (account.getStatus() == BLOCKED) {
            String message = "Account  " + account.getIban() + " is blocked. Withdrawing is not allowed";
            logger.debug(message);
            return new Response<>(account, singletonList(message));
        }

        if (account.getBalance().compareTo(amount) < 0) {
            String message = "Balance of account  " + account.getIban() + " is insufficient. " + account.getBalance() + " required " + amount;
            logger.debug(message);
            return new Response<>(account, singletonList(message));
        } else {
            account = updateBalance(accountId, amount.negate(), c);
            return new Response<>(account);
        }
    }

    @Override
    protected String getTableName() {
        return "Accounts";
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}

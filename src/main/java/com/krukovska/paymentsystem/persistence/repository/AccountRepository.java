package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public interface AccountRepository extends Repository<Account> {
    /**
     * updates balance of certain account
     *
     * @param accountId id of account which balance needs to be updates
     * @param amount    new amount of money on balance
     * @return updated account and list of error messages
     * @throws SQLException in case of database issues
     */
    Response<Account> updateBalance(long accountId, BigDecimal amount) throws SQLException;

    /**
     * updates balance of certain account in a given connection
     *
     * @param accountId id of account which balance needs to be updates
     * @param amount    new amount of money on balance
     * @param c         connection where update will be executed
     * @return updated account and list of error messages
     * @throws SQLException in case of database issues
     */
    Account updateBalance(long accountId, BigDecimal amount, Connection c) throws SQLException;

    /**
     * withdraws given amount of balance of certain account
     *
     * @param accountId id of account where money will be wirthdrawn
     * @param amount    amount that will be withdrawn
     * @return updated account and list of error messages
     * @throws SQLException in case of database issues
     */
    Response<Account> withdrawAccount(Long accountId, BigDecimal amount) throws SQLException;

    /**
     * withdraws given amount of balance of certain account in a given connection
     *
     * @param accountId id of account where money will be wirthdrawn
     * @param amount    amount that will be withdrawn
     * @param c         connection where update will be executed
     * @return updated account and list of error messages
     * @throws SQLException in case of database issues
     */
    Response<Account> withdrawAccount(Long accountId, BigDecimal amount, Connection c) throws SQLException;

}

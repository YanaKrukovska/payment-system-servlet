package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public interface AccountRepository extends Repository<Account> {
    Response<Account> updateBalance(long accountId, BigDecimal amount) throws SQLException;

    Account updateBalance(long accountId, BigDecimal amount, Connection c) throws SQLException;

    Response<Account> withdrawAccount(Long accountId, BigDecimal amount) throws SQLException;

    Response<Account> withdrawAccount(Long accountId, BigDecimal amount, Connection c) throws SQLException;

}

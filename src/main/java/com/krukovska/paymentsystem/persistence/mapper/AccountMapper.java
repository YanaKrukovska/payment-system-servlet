package com.krukovska.paymentsystem.persistence.mapper;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.AccountStatus;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.CreditCard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AccountMapper extends AbstractMapper <Account>{

    protected Account mapObject(ResultSet rs) throws SQLException {
        Objects.requireNonNull(rs, "ResultSet must be not null");

        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setIban(rs.getString("iban"));
        account.setName(rs.getString("name"));
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));
        Client client = new Client();
        client.setId(rs.getLong("client_id"));
        account.setClient(client);
        CreditCard creditCard = new CreditCard();
        creditCard.setId(rs.getLong("credit_card_id"));
        account.setCreditCard(creditCard);

        return account;
    }
}

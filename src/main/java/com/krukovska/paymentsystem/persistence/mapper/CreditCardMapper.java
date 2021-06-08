package com.krukovska.paymentsystem.persistence.mapper;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.CreditCard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CreditCardMapper extends AbstractMapper<CreditCard> {

    protected CreditCard mapObject(ResultSet rs) throws SQLException {
        Objects.requireNonNull(rs, "ResultSet must be not null");

        CreditCard creditCard = new CreditCard();
        creditCard.setId(rs.getLong("id"));

        creditCard.setCardNumber(rs.getString("card_number"));
        creditCard.setExpired(rs.getBoolean("is_expired"));
        Account account = new Account();
        account.setId(rs.getLong("account_id"));
        creditCard.setAccount(account);
        return creditCard;
    }
}

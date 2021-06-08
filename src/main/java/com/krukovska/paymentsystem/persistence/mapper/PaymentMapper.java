package com.krukovska.paymentsystem.persistence.mapper;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.PaymentStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PaymentMapper extends AbstractMapper<Payment> {

    protected Payment mapObject(ResultSet rs) throws SQLException {
        Objects.requireNonNull(rs, "ResultSet must be not null");

        Payment payment = new Payment();
        payment.setId(rs.getLong("id"));
        Account account = new Account();
        account.setId(rs.getLong("account_id"));
        payment.setAccount(account);
        payment.setAmount(BigDecimal.valueOf(rs.getDouble("amount")));
        payment.setStatus(PaymentStatus.valueOf(rs.getString("status").toUpperCase()));
        payment.setReceiverIban(rs.getString("receiver_iban"));
        payment.setDetails(rs.getString("details"));
        payment.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        return payment;
    }

    @Override
    public Set<Field> getFields(Payment p) {
        Objects.requireNonNull(p, "Entity must be not null");
        Set<Field> fields = new LinkedHashSet<>();
        fields.add(new Field("account_id", p.getAccount().getId()));
        fields.add(new Field("amount", p.getAmount()));
        fields.add(new Field("status", p.getStatus().name()));
        fields.add(new Field("receiver_iban", p.getReceiverIban()));
        fields.add(new Field("details", p.getDetails()));
        fields.add(new Field("payment_date", p.getPaymentDate()));

        return Collections.unmodifiableSet(fields);

    }

}

package com.krukovska.paymentsystem.persistence.mapper;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class UnblockRequestMapper extends AbstractMapper<UnblockRequest> {

    protected UnblockRequest mapObject(ResultSet rs) throws SQLException {
        Objects.requireNonNull(rs, "ResultSet must be not null");

        UnblockRequest request = new UnblockRequest();
        request.setId(rs.getLong("id"));

        if (rs.getDate("action_date") != null) {
            request.setActionDate(rs.getDate("action_date").toLocalDate());
        }
        request.setCreationDate(rs.getDate("creation_date").toLocalDate());
        Account account = new Account();
        account.setId(rs.getLong("account_id"));
        request.setAccount(account);
        Client client = new Client();
        client.setId(rs.getLong("client_id"));
        request.setClient(client);
        return request;
    }

    @Override
    public Set<Field> getFields(UnblockRequest p) {
        Objects.requireNonNull(p, "Entity must be not null");
        Set<Field> fields = new LinkedHashSet<>();
        fields.add(new Field("account_id", p.getAccount().getId()));
        fields.add(new Field("client_id", p.getClient().getId()));
        fields.add(new Field("creation_date", p.getCreationDate()));

        return Collections.unmodifiableSet(fields);

    }
}

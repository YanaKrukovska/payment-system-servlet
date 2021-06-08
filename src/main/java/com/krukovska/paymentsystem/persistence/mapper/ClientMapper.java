package com.krukovska.paymentsystem.persistence.mapper;

import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.ClientStatus;
import com.krukovska.paymentsystem.persistence.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ClientMapper extends AbstractMapper<Client> {

    protected Client mapObject(ResultSet rs) throws SQLException {
        Objects.requireNonNull(rs, "ResultSet must be not null");


        Client client = new Client();
        client.setId(rs.getLong("id"));
        User user = new User();
        user.setId(rs.getLong("user_id"));
        client.setUser(user);
        client.setStatus(ClientStatus.valueOf(rs.getString("status")));
        client.setAccounts(new ArrayList<>());
        return client;
    }
}

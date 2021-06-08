package com.krukovska.paymentsystem.persistence.mapper;

import com.krukovska.paymentsystem.persistence.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserMapper extends AbstractMapper<User> {

    protected User mapObject(ResultSet rs) throws SQLException {
        Objects.requireNonNull(rs, "ResultSet must be not null");

        User user = new User();
        user.setId(rs.getLong("id"));

        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setAdmin(rs.getBoolean("is_admin"));

        return user;
    }
}

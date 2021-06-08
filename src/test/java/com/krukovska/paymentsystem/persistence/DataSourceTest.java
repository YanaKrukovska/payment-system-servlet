package com.krukovska.paymentsystem.persistence;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataSourceTest {

    @Test
    void getConnection() throws SQLException {
        Connection connection = DataSource.getInstance().getConnection();
        assertNotNull(connection);
        connection.createStatement().executeQuery("SELECT 1");

    }

}
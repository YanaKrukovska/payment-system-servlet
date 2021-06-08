package com.krukovska.paymentsystem.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The singleton wrapper for HikariCP pooled DataSource.
 *
 */
public class DataSource {

    public static final String PROPERTY_FILE_NAME = "/datasource.properties";
    private final HikariDataSource ds;
    private static DataSource instance;

    /**
     * Construct a DataSource and uses datasource.properties file
     * for configuration
     * of the property file
     */
    private DataSource() {
        HikariConfig config = new HikariConfig(PROPERTY_FILE_NAME);
        ds = new HikariDataSource(config);
    }

    /**
     * Obtains DataSource instance
     */
    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    /**
     * Provides the db connection
     */
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}

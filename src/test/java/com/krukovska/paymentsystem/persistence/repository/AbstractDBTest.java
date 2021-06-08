package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

public class AbstractDBTest {
    protected Connection connection;

    AbstractDBTest() {
        try {
            connection = DataSource.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create SQL connection");
        }
    }

    @BeforeEach
    public void setUp() {
        runSQLScript("/scripts/create.sql");
        runSQLScript("/scripts/test-data.sql");
    }

    @AfterEach
    public void tearDown() {
        try {
            connection.createStatement().execute("DROP ALL OBJECTS");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    private void runSQLScript(String fileName) {
        InputStream is = this.getClass().getResourceAsStream(fileName);

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);

             BufferedReader reader = new BufferedReader(streamReader)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            connection.createStatement().execute(sb.toString());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}

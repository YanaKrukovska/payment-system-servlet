package com.krukovska.paymentsystem.persistence;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.krukovska.paymentsystem.persistence.SQLHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class SQLHelperTest {

    @Test
    void createSelectWhereById() {
        assertEquals("SELECT * FROM accounts WHERE id = ?;",
                createSelectById("accounts"));
    }

    @Test
    void createSelectAll() {
        assertEquals("SELECT * FROM accounts;",
                createSelect("accounts"));
    }

    @Test
    void createSelectByFieldSuccess() {
        assertEquals("SELECT * FROM accounts WHERE name = ?;",
                createSelectByField("accounts", new Field("name", "name_a")));
    }

    @Test
    void createCountSelectByFieldNotNullSuccess() {
        assertEquals("SELECT COUNT(*) FROM accounts WHERE name=?;",
                createCountSelectByField("accounts", new Field("name", "name_a")));
    }

    @Test
    void createCountSelectByFieldNullSuccess() {
        assertEquals("SELECT COUNT(*) FROM accounts WHERE name IS NULL;",
                createCountSelectByField("accounts", new Field("name", null)));
    }

    @Test
    void createCountSelectSuccess() {
        assertEquals("SELECT COUNT(*) FROM accounts;",
                createCountSelectByField("accounts"));
    }

    @Test
    void createPageableSelectFullNotNull() {

        assertEquals("SELECT * FROM accounts WHERE name = ? ORDER BY name ASC OFFSET 0 limit 10;",
                createPageableSelect("accounts",
                        new Field("name", "name_a"),
                        new PageAndSort(1, 10, PageAndSort.Direction.ASC, "name")));
    }

    @Test
    void createPageableSelectFullIsNull() {

        assertEquals("SELECT * FROM accounts WHERE name IS NULL ORDER BY name ASC OFFSET 0 limit 10;",
                createPageableSelect("accounts",
                        new Field("name", null),
                        new PageAndSort(1, 10, PageAndSort.Direction.ASC, "name")));
    }


    @Test
    void createUpdateSQLSingleField() {
        Set<Field> fields = new LinkedHashSet<>();
        fields.add(new Field("name", "name_a"));
        fields.add(new Field("client_id", 1L));
        assertEquals("UPDATE accounts SET name=?, client_id=? WHERE id=?;",
                createUpdateSQL("accounts", fields));
    }

    @Test
    void createUpdateSQLMultipleField() {
        assertEquals("UPDATE accounts SET name=? WHERE id=?;",
                createUpdateSQL("accounts", new Field("name", "account_name")));
    }

    @Test
    void createCountSelectNoTableName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                SQLHelper.createCountSelectByField(null, null));

        assertTrue(exception.getMessage().contains("tableName must be not empty"));
    }


    @Test
    void createCountSelectValueNotNull() {
        assertEquals("SELECT COUNT(*) FROM accounts WHERE clientId=?;",
                SQLHelper.createCountSelectByField("accounts", new Field("clientId", 100)));
    }

    @Test
    void createCountSelectValueIsNull() {
        assertEquals("SELECT COUNT(*) FROM accounts WHERE clientId IS NULL;",
                SQLHelper.createCountSelectByField("accounts", new Field("clientId", null)));
    }


    @Test
    void createInsert() {
        Set<Field> fields = new LinkedHashSet<>();
        fields.add(new Field("name", "name"));
        fields.add(new Field("status", "status"));
        fields.add(new Field("client_id", null));
        fields.add(new Field("details", "details"));
        assertEquals("INSERT INTO accounts (name,status,details) VALUES (?,?,?);",
                SQLHelper.createInsertSQL("accounts", fields));
    }

    @Test
    void createPageableSelectForPayments() {
        assertEquals("select p.*, a.client_id from  payments p left join accounts a on p.account_id = a.id where a.client_id = ?  ORDER BY name ASC OFFSET 0 limit 10;",
                SQLHelper.createPageableSelectForPayments("accounts", new PageAndSort(1, 10, PageAndSort.Direction.ASC, "name")));
    }
}

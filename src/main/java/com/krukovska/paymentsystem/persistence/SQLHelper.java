package com.krukovska.paymentsystem.persistence;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public class SQLHelper {
    private SQLHelper() {
    }

    /**
     * @param tableName table for which select query is created
     * @return generated select by id query for given table
     */
    public static String createSelectById(String tableName) {
        validateTableName(tableName);
        return "SELECT * FROM " + tableName + " WHERE id = ?;";
    }

    /**
     * @param tableName table for which update query is created
     * @param field     field that has to be updated
     * @return generated update field query for given table
     */
    public static String createUpdateSQL(String tableName, Field field) {
        return createUpdateSQL(tableName, new HashSet<>(singletonList(field)));
    }

    /**
     * @param tableName table for which update query is created
     * @param fields    fields that has to be updated
     * @return generated update fields query for given table
     */
    public static String createUpdateSQL(String tableName, Set<Field> fields) {
        validateTableName(tableName);
        validateFields(fields);
        StringBuilder sb = new StringBuilder("UPDATE ");

        String fieldNames = fields.stream().map(Field::getName).collect(Collectors.joining("=?, "));

        sb.append(tableName).
                append(" SET ").append(fieldNames).append("=?");

        return sb.append(" WHERE id=?;").toString();
    }

    /**
     * @param tableName table for which we create pageable select
     * @param field     field that we use for search
     * @param page      paging and sorting parameters
     * @return generated pageable select by field
     */
    public static String createPageableSelect(String tableName, Field field, PageAndSort page) {
        validateTableName(tableName);

        StringBuilder result = new StringBuilder("SELECT * FROM ").append(tableName);
        if (field != null) {
            result.append(" WHERE ")
                    .append(field.getName());

            if (field.getValue() == null) {
                result.append(" IS NULL");
            } else {
                result.append(" = ?");
            }
        }

        if (page != null) {
            addPageData(page, result);
        }
        result.append(";");
        return result.toString();

    }

    //TODO: add comment
    private static StringBuilder addPageData(PageAndSort page, StringBuilder sb) {
        sb.append(" ORDER BY ")
                .append(page.getProperty())
                .append(" ")
                .append(page.getDirection().name())
                .append(" OFFSET ")
                .append((page.getPage() - 1) * page.getSize())
                .append(" limit ")
                .append(page.getSize());
        return sb;
    }

    /**
     * @param tableName table for which we create not pageable select query
     * @return generated pageable select
     */
    public static String createSelect(String tableName) {
        return createPageableSelect(tableName, null, null);
    }

    /**
     * @param tableName table for which we create select by field query
     * @param field     field we use for search
     * @return generated select by field query
     */
    public static String createSelectByField(String tableName, Field field) {
        Objects.requireNonNull(field, "field must be not null");
        return createPageableSelect(tableName, field, null);
    }

    /**
     * @param tableName table for which we create count query
     * @return generated count query without field
     */
    public static String createCountSelectByField(String tableName) {
        validateTableName(tableName);
        return createCountSelectByField(tableName, null);
    }

    /**
     * @param tableName table for which we create count by field query
     * @param field     field we use to filter entries
     * @return generated count by field query without field
     */
    public static String createCountSelectByField(String tableName, Field field) {
        validateTableName(tableName);

        StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM ").append(tableName);
        if (field != null) {
            sb.append(" WHERE ").append(field.getName());
            if (field.getValue() == null) {
                sb.append(" IS NULL");
            } else {
                sb.append("=?");
            }
        }
        return sb.append(";").toString();
    }


    /**
     * @param tableName table for which we create count by field
     * @param fields    fields to insert
     * @return generated insert query
     */
    public static String createInsertSQL(String tableName, Set<Field> fields) {
        validateTableName(tableName);
        validateFields(fields);

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableName).
                append(" (").
                append(fields.stream().filter(field -> field.getValue() != null).map(Field::getName).collect(Collectors.joining(",")))
                .append(") VALUES (");

        long notEmptyFieldCount = fields.stream().filter(field -> field.getValue() != null).count();


        for (int i = 0; i < notEmptyFieldCount; i++) {
            sb.append("?,");
        }

        sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1).append(");");


        return sb.toString();
    }

    /**
     * @return generated update amount query
     */
    public static String createUpdateAmountSQL() {
        return "UPDATE Accounts  SET balance = balance + ?  WHERE id=?;";
    }

    /**
     * @param fields fields to validate
     */
    public static void validateFields(Set<Field> fields) {
        Objects.requireNonNull(fields, "field must be not null");

        if (fields.stream().map(Field::getName).anyMatch(StringUtils::isEmpty)) {
            throw new IllegalArgumentException("field name must be not empty");
        }
    }

    /**
     * check if table name is empty or not
     *
     * @param tableName table name that is checked
     */
    private static void validateTableName(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("tableName must be not empty");
        }
    }

    /**
     * @param tableName table for which we create count by field
     * @param page      sorting and paging parameters
     * @return generated pageable select query for payments
     */
    public static String createPageableSelectForPayments(String tableName, PageAndSort page) {
        validateTableName(tableName);
        StringBuilder sb = new StringBuilder("select p.*, a.client_id from  payments p left join accounts a on ");
        sb.append("p.account_id = a.id ").
                append("where a.client_id = ? ");
        addPageData(page, sb);
        return sb.append(";").toString();

    }

    /**
     * @return generated query to count client's payments
     */
    public static String createSelectPaymentCountForClient() {
        return "select count(*)  from  payments p left join accounts a on " + "p.account_id = a.id  where a.client_id = ? ;";
    }

}

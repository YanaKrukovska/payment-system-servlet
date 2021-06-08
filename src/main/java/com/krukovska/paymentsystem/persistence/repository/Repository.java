package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Entity;
import com.krukovska.paymentsystem.persistence.model.Response;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface Repository<T extends Entity> {
    T findById(long id) throws SQLException;

    T findById(long id, Connection c) throws SQLException;

    List<T> findByField(Field field, PageAndSort page) throws SQLException;

    List<T> findByField(Field field) throws SQLException;

    List<T> findAll(PageAndSort page) throws SQLException;

    Response<T> update(long id, Set<Field> fields) throws SQLException;

    Response<T> update(long id, Set<Field> fields, Connection c) throws SQLException;

    Response<T> update(long id, Field field) throws SQLException;

    Response<T> update(long id, Field field, Connection c) throws SQLException;

    long count() throws SQLException;

    long countByField(Field field) throws SQLException;

    T create(T entity) throws SQLException;
}

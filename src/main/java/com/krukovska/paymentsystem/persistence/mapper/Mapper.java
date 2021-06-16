package com.krukovska.paymentsystem.persistence.mapper;

import com.krukovska.paymentsystem.persistence.Field;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface Mapper<T> {

    /**
     * map one object from result set
     *
     * @param resultSet result set where we get object
     * @return mapped object
     * @throws SQLException if set contained more than one object
     */
    T mapSingleObject(ResultSet resultSet) throws SQLException;

    /**
     * map several objects from result set
     *
     * @param resultSet result set where we get object
     * @return mapped objects
     * @throws SQLException if something goes wrong with result set
     */
    List<T> mapObjects(ResultSet resultSet) throws SQLException;

    /**
     * @param entity where we find fields
     * @return set of entity fields
     */
    Set<Field> getFields(T entity);
}

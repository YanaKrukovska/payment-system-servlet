package com.krukovska.paymentsystem.persistence.mapper;

import com.krukovska.paymentsystem.persistence.Field;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractMapper<T> implements Mapper<T> {
    /**
     * map one object from result set
     *
     * @param rs result set where we get object
     * @return mapped object
     * @throws SQLException if set contained more than one object
     */
    @Override
    public T mapSingleObject(ResultSet rs) throws SQLException {
        Objects.requireNonNull(rs, "ResultSet must be not null");
        T result = null;
        if (rs.next()) {
            result = mapObject(rs);
            if (rs.next()) {
                throw new SQLException("Request has returned more then one object");
            }
        }
        return result;
    }

    /**
     * map several objects from result set
     *
     * @param rs result set where we get object
     * @return mapped objects
     * @throws SQLException if something goes wrong with result set
     */
    @Override
    public List<T> mapObjects(ResultSet rs) throws SQLException {
        List<T> items = new ArrayList<>();
        while (rs.next()) {
            items.add(mapObject(rs));
        }
        return items;
    }

    /**
     * @param entity where we find fields
     * @return set of entity fields
     */
    @Override
    public Set<Field> getFields(T entity) {
        throw new UnsupportedOperationException("Mapping to fields unsupported for " + entity.getClass().getName());
    }


    protected abstract T mapObject(ResultSet rs) throws SQLException;

}

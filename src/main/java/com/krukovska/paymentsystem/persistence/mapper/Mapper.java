package com.krukovska.paymentsystem.persistence.mapper;

import com.krukovska.paymentsystem.persistence.Field;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface Mapper<T> {
    T mapSingleObject(ResultSet resultSet) throws SQLException;
    List<T> mapObjects(ResultSet resultSet) throws SQLException;
    Set<Field> getFields(T entity);
}

package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.SQLHelper;
import com.krukovska.paymentsystem.persistence.mapper.Mapper;
import com.krukovska.paymentsystem.persistence.model.Entity;
import com.krukovska.paymentsystem.persistence.model.Response;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.krukovska.paymentsystem.persistence.SQLHelper.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public abstract class AbstractRepository<T extends Entity> implements Repository<T> {
    private final DataSource ds;
    private final Mapper<T> mapper;


    protected AbstractRepository(DataSource ds, Mapper<T> mapper) {
        this.ds = ds;
        this.mapper = mapper;
    }

    @Override
    public T findById(long id) throws SQLException {
        getLogger().debug("Finding by id = {}", id);

        try (Connection c = ds.getConnection()) {
            return findById(id, c);
        }
    }

    @Override
    public T findById(long id, Connection c) throws SQLException {
        try (PreparedStatement statement = c.prepareStatement(createSelectById(getTableName()))) {
            statement.setLong(1, id);
            return mapper.mapSingleObject(statement.executeQuery());
        }
    }

    @Override
    public Response<T> update(long id, Field field) throws SQLException {
        Objects.requireNonNull(field, "field must be not null");
        return update(id, new HashSet<>(Collections.singletonList(field)));
    }


    @Override
    public Response<T> update(long id, Field field, Connection c) throws SQLException {
        Objects.requireNonNull(field, "field must be not null");
        return update(id, new HashSet<>(Collections.singletonList(field)), c);
    }


    @Override
    public Response<T> update(long id, Set<Field> fields) throws SQLException {
        getLogger().debug("Updating entity with id = {} ", id);
        Objects.requireNonNull(fields, "fields must be not null");

        try (Connection c = ds.getConnection()) {
            return update(id, fields, c);
        }
    }


    @Override
    public Response<T> update(long id, Set<Field> fields, Connection c) throws SQLException {
        getLogger().debug("Updating entity with id = {} ", id);
        Objects.requireNonNull(fields, "fields must be not null");
        Objects.requireNonNull(c, "Connection must be not null");

        logFields(fields);

        try (
                PreparedStatement statement =
                        c.prepareStatement(SQLHelper.createUpdateSQL(getTableName(), fields))
        ) {
            int counter = 1;

            T entity = findById(id, c);
            if (entity == null) {
                String message = "Entity does not exist. id = " + id;
                getLogger().debug(message);
                return new Response<>(singletonList(message));
            }


            for (Object value : fields.stream().map(Field::getValue).collect(Collectors.toList())) {
                Object valueToSet = value;
                if (value instanceof Enum) {
                    valueToSet = ((Enum<?>) value).name();
                }

                statement.setObject(counter++, valueToSet);
            }
            statement.setLong(counter, id);
            statement.executeUpdate();
            return new Response<>(findById(id, c));

        }
    }

    @Override
    public T create(T entity) throws SQLException {
        getLogger().debug("creating entity {}", entity);
        Objects.requireNonNull(entity, "Entity must be not null");

        Set<Field> fieldsForCreating = mapper.getFields(entity);

        logFields(fieldsForCreating);

        try (Connection c = ds.getConnection();
             PreparedStatement statement =
                     c.prepareStatement(SQLHelper.createInsertSQL(getTableName(),
                             fieldsForCreating), Statement.RETURN_GENERATED_KEYS)) {

            int counter = 1;

            for (Field field : fieldsForCreating) {
                Object valueToSet = field.getValue();
                if (valueToSet instanceof Enum) {
                    valueToSet = ((Enum<?>) valueToSet).name();
                }

                statement.setObject(counter++, valueToSet);
            }
            if (statement.executeUpdate() == 1) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                return findById(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating failed. Entity: " + entity);
            }
        }
    }


    @Override
    public List<T> findByField(Field field, PageAndSort page) throws SQLException {

        Objects.requireNonNull(field, "field must be not null ");
        Objects.requireNonNull(page, "Page must be not null ");

        getLogger().debug("Finding entity by field={},  page(number/size) :{}", field, page);

        return queryForObjects(createPageableSelect(getTableName(), field, page), singletonList(field.getValue()));

    }

    @Override
    public List<T> findByField(Field field) throws SQLException {
        Objects.requireNonNull(field, "field must be not null ");
        getLogger().debug("Finding entity by field={} ", field);

        return queryForObjects(createSelectByField(getTableName(), field), singletonList(field.getValue()));
    }

    @Override
    public List<T> findAll(PageAndSort page) throws SQLException {
        Objects.requireNonNull(page, "page must be not null ");
        getLogger().debug("Getting items for page={} ", page);

        return queryForObjects(createPageableSelect(getTableName(), null, page));

    }


    @Override
    public long countByField(Field field) throws SQLException {
        Objects.requireNonNull(field, "field must be not empty");
        getLogger().debug("Count entity by field={}", field);
        return count(createCountSelectByField(getTableName(), field), field);
    }

    @Override
    public long count() throws SQLException {
        getLogger().debug("Getting count");
        return count(createCountSelectByField(getTableName()), null);
    }


    protected long count(String sql, Field field) throws SQLException {
        try (Connection c = ds.getConnection();
             PreparedStatement statement =
                     c.prepareStatement(sql)) {
            if (field != null) {
                statement.setObject(1, field.getValue());
            }
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getLong(1);
        }
    }

    private List<T> queryForObjects(String sql) throws SQLException {
        return queryForObjects(sql, emptyList());
    }

    protected List<T> queryForObjects(String sql, List<Object> values) throws SQLException {

        try (Connection c = ds.getConnection();
             PreparedStatement statement = c.prepareStatement(sql)) {

            if (values != null && !values.isEmpty()) {
                int counter = 1;
                for (Object v : values) {
                    statement.setObject(counter++, v);
                }
            }
            ResultSet rs = statement.executeQuery();

            return mapper.mapObjects(rs);

        }
    }

    protected abstract String getTableName();

    protected abstract Logger getLogger();

    private void logFields(Set<Field> fieldsForCreating) {
        String valuesForUpdate = fieldsForCreating.stream().map(Field::toString).collect(Collectors.joining());
        getLogger().debug("Entity with fields = {}", valuesForUpdate);
    }

}

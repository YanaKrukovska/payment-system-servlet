package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.mapper.Mapper;
import com.krukovska.paymentsystem.persistence.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserRepository extends AbstractRepository<User> {

    private static final Logger logger = LogManager.getLogger(UserRepository.class);

    public UserRepository(DataSource ds, Mapper<User> mapper) {
        super(ds, mapper);
    }

    @Override
    protected String getTableName() {
        return "Users";
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}

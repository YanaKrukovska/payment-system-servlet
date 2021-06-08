package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.mapper.Mapper;
import com.krukovska.paymentsystem.persistence.model.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientRepository extends AbstractRepository<Client> {

    private static final Logger logger = LogManager.getLogger(ClientRepository.class);

    public ClientRepository(DataSource ds, Mapper<Client> mapper) {
        super(ds, mapper);
    }

    @Override
    protected String getTableName() {
        return "Clients";
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }


}

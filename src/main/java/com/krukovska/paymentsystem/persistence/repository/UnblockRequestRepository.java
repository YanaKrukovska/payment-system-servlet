package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.mapper.Mapper;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnblockRequestRepository extends AbstractRepository<UnblockRequest> {

    private static final Logger logger = LogManager.getLogger(UnblockRequestRepository.class);

    public UnblockRequestRepository(DataSource ds, Mapper<UnblockRequest> mapper) {
        super(ds, mapper);
    }

    @Override
    protected String getTableName() {
        return "unblock_requests";
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}

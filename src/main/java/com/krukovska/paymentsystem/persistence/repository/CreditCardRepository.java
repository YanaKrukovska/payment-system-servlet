package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.mapper.Mapper;
import com.krukovska.paymentsystem.persistence.model.CreditCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreditCardRepository extends AbstractRepository<CreditCard> {

    private static final Logger logger = LogManager.getLogger(CreditCardRepository.class);

    public CreditCardRepository(DataSource ds, Mapper<CreditCard> mapper) {
        super(ds, mapper);
    }

    @Override
    protected String getTableName() {
        return "credit_cards";
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}

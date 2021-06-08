package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.model.CreditCard;
import com.krukovska.paymentsystem.persistence.repository.CreditCardRepository;
import com.krukovska.paymentsystem.service.CreditCardService;
import com.krukovska.paymentsystem.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class CreditCardServiceImpl implements CreditCardService {

    private static final Logger logger = LogManager.getLogger(CreditCardServiceImpl.class);
    private final CreditCardRepository repository;


    public CreditCardServiceImpl(CreditCardRepository repository) {
        this.repository = repository;
    }

    @Override
    public CreditCard findCardByCardNumber(String cardNumber) {
        logger.debug("getting card by number {}", cardNumber);
        try {
            List<CreditCard> cards = repository.findByField(new Field("card_number",
                    cardNumber));
            if (cards.isEmpty()) {
                return null;
            }
            return cards.get(0);
        } catch (SQLException e) {
            String message = "Error during getting credit card  by number: " + cardNumber;
            logger.error(message, e);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public CreditCard findCreditCardByAccountId(Long accountId) {
        logger.debug("getting credit cards by accountId {}", accountId);
        try {
            return repository.findByField(new Field("account_id", accountId)).get(0);
        } catch (SQLException e) {
            String message = "Error during getting credit card  accountId: " + accountId;
            logger.error(message, e);
            throw new ServiceException(message, e);
        }
    }
}

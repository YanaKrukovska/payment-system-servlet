package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.CreditCard;

public interface CreditCardService {

    /**
     * find credit card by it's number
     *
     * @param cardNumber number of card that needs to be found
     * @return found credit card
     */
    CreditCard findCardByCardNumber(String cardNumber);

    /**
     * finds credit card by given account id
     *
     * @param accountId id of account that the searched card belongs to
     * @return found credit card
     */
    CreditCard findCreditCardByAccountId(Long accountId);
}

package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.CreditCard;

public interface CreditCardService {

    CreditCard findCardByCardNumber(String cardNumber);

    CreditCard findCreditCardByAccountId(Long accountId);
}

package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.model.CreditCard;
import com.krukovska.paymentsystem.persistence.repository.CreditCardRepository;
import com.krukovska.paymentsystem.service.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCardServiceImplTest {
    @Mock
    private CreditCardRepository repo;
    @InjectMocks
    private CreditCardServiceImpl service;



    @Test
    void findCardByCardNumberSuccess() throws SQLException {
        when(repo.findByField(any(Field.class))).thenReturn(Collections.singletonList(new CreditCard()));
        service.findCardByCardNumber("111");

        verify(repo, times(1)).findByField(any(Field.class));
    }

    @Test
    void findCardByCardNumberFail() throws SQLException {
        when(repo.findByField(any(Field.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.findCardByCardNumber("111")
        );
        verify(repo, times(1)).findByField(any(Field.class));
        assertThat(exception.getMessage(), equalTo("Error during getting credit card  by number: 111"));
    }


    @Test
    void findCreditCardByAccountIdSuccess() throws SQLException {
        when(repo.findByField(any(Field.class))).thenReturn(Collections.singletonList(new CreditCard()));
        service.findCreditCardByAccountId(1L);

        verify(repo, times(1)).findByField(any(Field.class));
    }

    @Test
    void findCreditCardByAccountIdFail() throws SQLException {
        when(repo.findByField(any(Field.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.findCreditCardByAccountId(1L)
        );
        verify(repo, times(1)).findByField(any(Field.class));
        assertThat(exception.getMessage(), equalTo("Error during getting credit card  accountId: 1"));
    }

}
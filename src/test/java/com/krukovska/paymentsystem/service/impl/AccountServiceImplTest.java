package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.AccountRepository;
import com.krukovska.paymentsystem.service.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository repo;

    @InjectMocks
    private AccountServiceImpl service;
    private final PageAndSort defaultPage = new PageAndSort(1, 5, PageAndSort.Direction.DESC, "id");

    @Test
    void findAllClientAccountsSuccess() throws SQLException {

        service.findAllClientAccounts(1L, defaultPage);
        verify(repo, times(1)).findByField(any(Field.class), any(PageAndSort.class));
    }

    @Test
    void findAllClientAccountsFail() throws SQLException {
        when(repo.findByField(any(Field.class), any(PageAndSort.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.findAllClientAccounts(1L, defaultPage)
        );
        verify(repo, times(1)).findByField(any(Field.class), any(PageAndSort.class));
        assertTrue(exception.getMessage().contains("Error during getting accounts for client with id=1"));
    }


    @Test
    void findAccountByIdSuccess() throws SQLException {

        service.findAccountById(1L);
        verify(repo, times(1)).findById(anyLong());
    }

    @Test
    void findAccountByIdFail() throws SQLException {
        when(repo.findById(anyLong())).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.findAccountById(1L)
        );
        verify(repo, times(1)).findById(anyLong());
        assertTrue(exception.getMessage().contains("Error during getting account with id=1"));
    }

    @Test
    void topUpAccountFail() throws SQLException {
        when(repo.updateBalance(anyLong(), any(BigDecimal.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.topUpAccount(1L, BigDecimal.ONE)
        );
        verify(repo, times(1)).updateBalance(anyLong(), any(BigDecimal.class));
        assertTrue(exception.getMessage().contains("Error during top up account with id=1"));
    }

    @Test
    void topUpAccountFailLessThan0() throws SQLException {
        Response<Account> result = service.topUpAccount(1L, BigDecimal.valueOf(-100));
        assertThat(result.getErrors().get(0), equalTo("You can't add less than 0"));
        verify(repo, never()).updateBalance(anyLong(), any(BigDecimal.class));
    }

    @Test
    void topUpAccountFail0() throws SQLException {
        Response<Account> result = service.topUpAccount(1L, BigDecimal.ZERO);
        assertThat(result.getErrors().get(0), equalTo("You can't add less than 0"));
        verify(repo, never()).updateBalance(anyLong(), any(BigDecimal.class));
    }

    @Test
    void topUpAccountSuccess() throws SQLException {
        Response<Account> response = new Response<>();
        when(repo.updateBalance(anyLong(), any(BigDecimal.class))).thenReturn(response);

        Response<Account> result = service.topUpAccount(1L, BigDecimal.TEN);
        assertThat(result.getErrors(), empty());
        verify(repo, times(1)).updateBalance(anyLong(), any(BigDecimal.class));
    }

    @Test
    void blockAccountFail() throws SQLException {
        when(repo.update(anyLong(), any(Field.class))).thenThrow(new SQLException());

        Response<Account> result = service.blockAccount(1L);
        assertThat(result.getErrors().get(0), equalTo("Error during blocking account 1"));

        verify(repo, times(1)).update(eq(1L), any(Field.class));

    }


    @Test
    void blockAccountSuccess() throws SQLException {

        when(repo.update(anyLong(), any(Field.class))).thenReturn(new Response<>());


        Response<Account> result = service.blockAccount(1L);
        assertThat(result.getErrors(), empty());
        verify(repo, times(1)).update(eq(1L), any(Field.class));

    }

    @Test
    void unblockAccountFail() throws SQLException {
        when(repo.update(anyLong(), any(Field.class))).thenThrow(new SQLException());


        Response<Account> result = service.unblockAccount(1L);
        assertThat(result.getErrors().get(0), equalTo("Error during unblocking account 1"));

        verify(repo, times(1)).update(eq(1L), any(Field.class));

    }


    @Test
    void unblockAccountSuccess() throws SQLException {

        when(repo.update(anyLong(), any(Field.class))).thenReturn(new Response<>());


        Response<Account> result = service.unblockAccount(1L);
        assertThat(result.getErrors(), empty());
        verify(repo, times(1)).update(eq(1L), any(Field.class));

    }

    @Test
    void countByClientIdFail() throws SQLException {
        when(repo.countByField(any(Field.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.countByClientId(1L)
        );
        verify(repo, times(1)).countByField(any(Field.class));
        assertTrue(exception.getMessage().contains("Error during counting accounts for clientId=1"));


    }


    @Test
    void countByClientIdSuccess() throws SQLException {

        when(repo.countByField(any(Field.class))).thenReturn(100L);

        assertEquals(100L, service.countByClientId(1L));
        verify(repo, times(1)).countByField(any(Field.class));

    }

    @Test
    void withdrawAccountSuccessfully() throws SQLException {
        Response<Account> response = new Response<>();
        when(repo.withdrawAccount(anyLong(), any(BigDecimal.class))).thenReturn(response);

        Response<Account> result = service.withdrawAccount(1L, BigDecimal.ONE);
        verify(repo, times(1)).withdrawAccount(anyLong(), any(BigDecimal.class));
        assertThat(result.getErrors(), empty());
    }

    @Test
    void withdrawAccountNegativeAmount() throws SQLException {
        Response<Account> result = service.withdrawAccount(1L, BigDecimal.valueOf(-100));
        verify(repo, never()).withdrawAccount(anyLong(), any(BigDecimal.class));
        assertThat(result.getErrors().get(0), equalTo("Sum must be > 0. current amount =  -100"));
    }


    @Test
    void withdrawAccountException() throws SQLException {
        when(repo.withdrawAccount(anyLong(), any(BigDecimal.class))).thenThrow(new SQLException());

        Response<Account> result = service.withdrawAccount(1L, BigDecimal.ONE);
        verify(repo, times(1)).withdrawAccount(anyLong(), any(BigDecimal.class));
        assertThat(result.getErrors().get(0), equalTo("Error during withdraw account 1 with amount = 1"));
    }

}
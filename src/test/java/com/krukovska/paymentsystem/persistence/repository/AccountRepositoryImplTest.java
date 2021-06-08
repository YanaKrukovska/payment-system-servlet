package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.mapper.AccountMapper;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountRepositoryImplTest extends AbstractDBTest {

    private final AccountRepositoryImpl repository = new AccountRepositoryImpl(DataSource.getInstance(), new AccountMapper());

    @Test
    void findById() throws SQLException {
        Account result = repository.findById(1L);
        Assertions.assertNotNull(result);
    }

    @Test
    void updateSuccessfully() throws SQLException {
        assertEquals(0, BigDecimal.valueOf(1000000).compareTo(repository.
                update(1L, new Field("balance", BigDecimal.valueOf(1000000))).getObject().getBalance()));
    }

    @Test
    void getSortedAndPageableByNameAscPage1() throws SQLException {
        List<Account> result = repository.findByField(new Field("client_id", 1),
                new PageAndSort(1, 5, PageAndSort.Direction.ASC, "name"));

        assertThat(result.stream().map(Account::getName).collect(toList()),
                contains("account_10", "account_11", "account_12", "account_13", "account_14"));
    }

    @Test
    void getSortedAndPageableByNameAscPage2() throws SQLException {
        List<Account> result = repository.findByField(new Field("client_id", 1),
                new PageAndSort(2, 5, PageAndSort.Direction.ASC, "name"));

        assertThat(result.stream().map(Account::getName).collect(toList()),
                contains("account_15", "account_16", "account_17", "account_18", "account_19"));
    }

    @Test
    void getSortedAndPageableByNameAscPageNotComplete() throws SQLException {
        List<Account> result = repository.findByField(new Field("client_id", 1),
                new PageAndSort(4, 6, PageAndSort.Direction.ASC, "name"));

        assertThat(result.stream().map(Account::getName).collect(toList()),
                contains("account_9", "salary"));
    }

    @Test
    void getSortedAndPageableByNameAscPageNotExist() throws SQLException {
        List<Account> result = repository.findByField(new Field("client_id", 1),
                new PageAndSort(5, 6, PageAndSort.Direction.ASC, "name"));

        assertThat(result, empty());
    }


    @Test
    void getSortedAndPageableByNameDescPage1() throws SQLException {
        List<Account> result = repository.findByField(new Field("client_id", 1),
                new PageAndSort(1, 5, PageAndSort.Direction.DESC, "name"));

        assertThat(result.stream().map(Account::getName).collect(toList()),
                contains("salary", "account_9", "account_8", "account_7", "account_6"));
    }


    @Test
    void getSortedAndPageableByNameDescPage4() throws SQLException {
        List<Account> result = repository.findByField(new Field("client_id", 1),
                new PageAndSort(4, 6, PageAndSort.Direction.DESC, "name"));

        assertThat(result.stream().map(Account::getName).collect(toList()),
                contains("account_11", "account_10"));
    }


    @Test
    void getSortedAndPageableByIdAscPage1() throws SQLException {
        List<Account> result = repository.findByField(new Field("client_id", 1),
                new PageAndSort(1, 5, PageAndSort.Direction.ASC, "id"));

        assertThat(result.stream().map(Account::getId).collect(toList()),
                contains(1L, 4L, 5L, 6L, 7L));

    }

    @Test
    void getSortedAndPageableByIdAscPage3() throws SQLException {
        List<Account> result = repository.findByField(new Field("client_id", 1),
                new PageAndSort(4, 5, PageAndSort.Direction.ASC, "id"));

        assertThat(result.stream().map(Account::getId).collect(toList()),
                contains(18L, 19L, 20L, 21L, 22L));
    }

    @Test
    void getSortedAndPageableByIdDescPage1() throws SQLException {
        List<Account> result = repository.findByField(new Field("client_id", 1),
                new PageAndSort(1, 5, PageAndSort.Direction.DESC, "id"));

        assertThat(result.stream().map(Account::getId).collect(toList()),
                contains(22L, 21L, 20L, 19L, 18L));
    }

    @Test
    void getCount() throws SQLException {
        Long count = repository.countByField(new Field("client_id", 1L));
        assertThat(count, equalTo(20L));
    }

    @Test
    void getCountNotExistingClient() throws SQLException {
        Long count = repository.countByField(new Field("client_id", 140L));
        assertThat(count, equalTo(0L));
    }

    @Test
    void updateBalanceSuccess() throws SQLException {
        Response<Account> result = repository.updateBalance(1L, BigDecimal.valueOf(600));
        assertThat(result.getErrors(), hasSize(0));
        assertThat(result.getObject().getBalance().compareTo(BigDecimal.valueOf(3100)), equalTo(0));
    }

    @Test
    void updateBalanceError() throws SQLException {
        Response<Account> result = repository.updateBalance(1000L, BigDecimal.valueOf(600));
        assertThat(result.getErrors(), hasSize(1));
        assertThat(result.getErrors(), contains("Account with id 1000 does not exist"));
        assertThat(result.getObject(), nullValue());
    }

    @Test
    void withdrawAmountSuccess() throws SQLException {
        Response<Account> result = repository.withdrawAccount(1L, BigDecimal.valueOf(50));
        assertThat(result.getErrors(), hasSize(0));
        assertThat(result.getObject().getBalance().compareTo(BigDecimal.valueOf(2450)), equalTo(0));
    }

    @Test
    void withdrawAmountBlocked() throws SQLException {
        Response<Account> result = repository.withdrawAccount(4L, BigDecimal.valueOf(50));
        assertThat(result.getErrors(), hasSize(1));
        assertThat(result.getErrors(), contains("Account  UA00000_4 is blocked. Withdrawing is not allowed"));
    }

    @Test
    void withdrawAmountNotEnoughMoney() throws SQLException {
        Response<Account> result = repository.withdrawAccount(1L, BigDecimal.valueOf(50000));
        assertThat(result.getErrors(), hasSize(1));
        assertThat(result.getErrors(), contains("Balance of account  UA2532820912340000056789 is insufficient. 2500.00 required 50000"));
    }


    @Test
    void withdrawAmountNotExist() throws SQLException {
        Response<Account> result = repository.withdrawAccount(166L, BigDecimal.valueOf(5));
        assertThat(result.getErrors(), hasSize(1));
        assertThat(result.getErrors(), contains("Account with id 166 does not exist"));
    }


    @Test
    void getTableName() {
        assertThat(repository.getTableName(), equalToIgnoringCase("accounts"));
    }
}
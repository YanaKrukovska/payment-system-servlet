package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.mapper.PaymentMapper;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.PaymentStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryImplTest extends AbstractDBTest {
    private final PaymentRepositoryImpl repository = new PaymentRepositoryImpl(DataSource.getInstance(), new PaymentMapper());

    @Test
    void findById() throws SQLException {
        Payment result = repository.findById(1L);
        assertNotNull(result);
    }

    @Test
    void countForClient() throws SQLException {
        assertEquals(8L, repository.countByClient(2L));
    }

    @Test
    void updatePaymentNullField() throws SQLException {
        Response<Payment> result = repository.update(1L, new Field("status", PaymentStatus.CREATED));
        assertThat(result.getObject().getStatus(), equalTo(PaymentStatus.CREATED));

    }

    @Test
    void createSuccess() throws SQLException {
        Payment newPayment = new Payment();
        Account account = new Account();
        account.setId(1L);
        newPayment.setAccount(account);
        newPayment.setAmount(BigDecimal.valueOf(100));
        newPayment.setStatus(PaymentStatus.CREATED);
        newPayment.setReceiverIban("UA2605650656");
        newPayment.setDetails("payment fee");
        newPayment.setPaymentDate(LocalDate.now());

        Payment result = repository.create(newPayment);
        assertThat(result.getId(), equalTo(9L));
        assertThat(result.getStatus(), equalTo(PaymentStatus.CREATED));
        assertThat(result.getReceiverIban(), equalTo("UA2605650656"));
        assertThat(result.getDetails(), equalTo("payment fee"));
    }

    @Test
    void updateSuccessfully() throws SQLException {
        assertEquals(0, BigDecimal.valueOf(20000).compareTo(repository.update(1L, new Field("amount", BigDecimal.valueOf(20000))).getObject().getAmount()));
    }

    @Test
    void createPaymentSuccessfully() throws SQLException {
        Payment payment = new Payment();

        Account account = new Account();
        account.setId(1L);
        payment.setAccount(account);
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.CREATED);
        payment.setReceiverIban("UA2605650656");
        payment.setDetails("payment fee");
        payment.setPaymentDate(LocalDate.now());
        Payment result = repository.create(payment);
        assertTrue(result.getId() > 0);
        assertEquals("UA2605650656", result.getReceiverIban());
    }


    @Test
    void getSortedAndPageableByNameAscPage1() throws SQLException {
        List<Payment> result = repository.findClientPayments(2L,
                new PageAndSort(1, 5, PageAndSort.Direction.ASC, "id"));

        assertThat(result.stream().map(Payment::getId).collect(toList()),
                contains(1L, 2L, 3L, 4L, 5L));
    }


    @Test
    void sentIfAlreadySentFail() throws SQLException {
        Response<Payment> result = repository.send(1L);
        assertThat(result.getErrors(), hasSize(1));
        assertThat(result.getErrors(), contains("Payment with id 1 is already sent"));
    }


    @Test
    void sentIfNotExistFail() throws SQLException {
        Response<Payment> result = repository.send(111L);
        assertThat(result.getErrors(), hasSize(1));
        assertThat(result.getErrors(), contains("Payment with id 111 does not exist"));
    }


    @Test
    void sentSuccess() throws SQLException {
        Response<Payment> result = repository.send(5L);
        assertThat(result.getErrors(), empty());
        assertThat(result.getObject().getStatus(), equalTo(PaymentStatus.SENT));
    }

    @Test
    void sentNotEnoughMoney() throws SQLException {
        Response<Payment> result = repository.send(8L);
        assertThat(result.getErrors(), hasSize(1));
        assertThat(result.getErrors(), contains("Balance of account  UA2532820112340010056781 is insufficient. 40000.00 required 99999.0"));
    }

}
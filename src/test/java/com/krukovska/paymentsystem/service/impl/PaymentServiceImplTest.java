package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.repository.PaymentRepository;
import com.krukovska.paymentsystem.service.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository repo;
    @InjectMocks
    private PaymentServiceImpl service;

    private final PageAndSort defaultPage = new PageAndSort(1, 5, PageAndSort.Direction.DESC, "id");

    @Test
    void findAllClientPaymentsSuccess() throws SQLException {
        service.findAllClientPayments(1L, defaultPage);
        verify(repo, times(1)).findClientPayments(anyLong(), any(PageAndSort.class));
    }

    @Test
    void findAllClientPaymentsFail() throws SQLException {
        when(repo.findClientPayments(anyLong(), any(PageAndSort.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.findAllClientPayments(1L, defaultPage)
        );
        verify(repo, times(1)).findClientPayments(anyLong(), any(PageAndSort.class));
        assertTrue(exception.getMessage().contains("Error during getting payments for client with id=1"));
    }

    @Test
    void createPaymentsSuccess() throws SQLException {
        service.create(new Payment());
        verify(repo, times(1)).create(any(Payment.class));
    }

    @Test
    void createPaymentsFail() throws SQLException {
        when(repo.create(any(Payment.class))).thenThrow(new SQLException());
        Payment p = new Payment();
        Exception exception = assertThrows(ServiceException.class, () ->
                service.create(p)
        );
        verify(repo, times(1)).create(any(Payment.class));
        assertTrue(exception.getMessage().contains("Error during creating  new payment ="));
    }

    @Test
    void sentPaymentsSuccess() throws SQLException {
        service.send(anyLong());
        verify(repo, times(1)).send(anyLong());
    }

    @Test
    void sentPaymentsFail() throws SQLException {
        when(repo.send(anyLong())).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.send(1L)
        );
        verify(repo, times(1)).send(anyLong());
        assertTrue(exception.getMessage().contains("Error during sending  payment with id =1"));
    }

    @Test
    void countByClientIdSuccess() throws SQLException {
        service.count(1L);
        verify(repo, times(1)).countByClient(anyLong());

    }
    @Test
    void countByClientIdFail() throws SQLException {
        when(repo.countByClient(anyLong())).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.count(1L)
        );
        verify(repo, times(1)).countByClient(anyLong());
        assertTrue(exception.getMessage().contains("Error during sending  payment with id =1"));


    }

}
package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;
import com.krukovska.paymentsystem.persistence.repository.UnblockRequestRepository;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnblockRequestServiceImplTest {

    @Mock
    private UnblockRequestRepository repo;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private UnblockRequestServiceImpl service;

    private final PageAndSort defaultPage = new PageAndSort(1, 5, PageAndSort.Direction.DESC, "id");

    @Test
    void findRequestByIdSuccess() throws SQLException {
        service.findRequestById(1L);
        verify(repo, times(1)).findById(anyLong());
    }

    @Test
    void findRequestByIdFail() throws SQLException {
        when(repo.findById(anyLong())).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.findRequestById(1L)
        );
        verify(repo, times(1)).findById(anyLong());
        assertTrue(exception.getMessage().contains("Error during getting request with id=1"));
    }

    @Test
    void createNewRequestSuccess() throws SQLException {
        service.createNewRequest(1L, 2L);
        verify(repo, times(1)).create(any(UnblockRequest.class));
    }

    @Test
    void createNewRequestFail() throws SQLException {
        when(repo.create(any(UnblockRequest.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.createNewRequest(1L, 2L)
        );
        verify(repo, times(1)).create(any(UnblockRequest.class));
        assertTrue(exception.getMessage().contains("Error during creating new request ="));
    }


    @Test
    void findAllClientRequestsSuccess() throws SQLException {

        service.findAllClientRequests(1L, defaultPage);
        verify(repo, times(1)).findByField(any(Field.class), any(PageAndSort.class));
    }

    @Test
    void findAllClientRequestsFail() throws SQLException {
        when(repo.findByField(any(Field.class), any(PageAndSort.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.findAllClientRequests(1L, defaultPage)
        );
        verify(repo, times(1)).findByField(any(Field.class), any(PageAndSort.class));
        assertTrue(exception.getMessage().contains("Error during getting requests for client with id=1"));
    }

    @Test
    void findAllRequestsSuccess() throws SQLException {

        service.findAllRequests(defaultPage);
        verify(repo, times(1)).findAll(any(PageAndSort.class));
    }

    @Test
    void findAllRequestsFail() throws SQLException {
        when(repo.findAll(any(PageAndSort.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.findAllRequests(defaultPage)
        );
        verify(repo, times(1)).findAll(any(PageAndSort.class));
        assertTrue(exception.getMessage().contains("Error during getting all requests"));
    }

    @Test
    void countSuccess() throws SQLException {

        service.countAll();
        verify(repo, times(1)).count();
    }

    @Test
    void countFail() throws SQLException {
        when(repo.count()).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.countAll()
        );
        verify(repo, times(1)).count();
        assertTrue(exception.getMessage().contains("Error during counting all requests"));
    }

    @Test
    void acceptRequestSuccess() throws SQLException {

        UnblockRequest request = new UnblockRequest();
        Account account = new Account();
        account.setId(1L);
        request.setAccount(account);
        when(repo.findById(anyLong())).thenReturn(request);

        service.acceptRequest(1L);

        verify(repo, times(1)).update(anyLong(), any(Field.class));
        verify(accountService, times(1)).unblockAccount(anyLong());
    }


    @Test
    void acceptRequestNotExist() throws SQLException {


        when(repo.findById(anyLong())).thenReturn(null);

        Response<UnblockRequest> result = service.acceptRequest(1L);
        assertThat(result.getErrors().get(0), equalTo("Unblock request with id 1 does not exist"));

        verify(repo, never() ).update(anyLong(), any(Field.class));
        verify(accountService, never()).unblockAccount(anyLong());
    }

    @Test
    void acceptRequestFail() throws SQLException {
        UnblockRequest request = new UnblockRequest();
        Account account = new Account();
        account.setId(1L);
        request.setAccount(account);

        when(repo.findById(anyLong())).thenReturn(request);

        when(repo.update(anyLong(),any(Field.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.acceptRequest(1L)
        );
        verify(repo, times(1)).update(anyLong(), any(Field.class));
        verify(accountService, times(1)).unblockAccount(anyLong());
        assertTrue(exception.getMessage().contains("Error during updating request ="));


    }
}
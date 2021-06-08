package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.ClientStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.ClientRepository;
import com.krukovska.paymentsystem.service.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {


    @Mock
    private ClientRepository repo;
    @InjectMocks
    private ClientServiceImpl service;
    private final PageAndSort defaultPage = new PageAndSort(1, 5, PageAndSort.Direction.DESC, "id");


    @Test
    void findAccountByIdSuccess() throws SQLException {

        service.findClientById(1L);
        verify(repo, times(1)).findById(anyLong());
    }

    @Test
    void findAccountByIdFail() throws SQLException {
        when(repo.findById(anyLong())).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.findClientById(1L)
        );
        verify(repo, times(1)).findById(anyLong());
        assertTrue(exception.getMessage().contains("Error during getting client with id=1"));
    }


    @Test
    void findAllClientsSuccess() throws SQLException {

        service.findAllClients(defaultPage);
        verify(repo, times(1)).findAll(any(PageAndSort.class));
    }

    @Test
    void findAllClientsFail() throws SQLException {
        when(repo.findAll(any(PageAndSort.class))).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.findAllClients(defaultPage)
        );
        verify(repo, times(1)).findAll(any(PageAndSort.class));
        assertTrue(exception.getMessage().contains("Error during getting all clients for page"));
    }

    @Test
    void blockClientSuccess() throws SQLException {
        Client client = new Client();
        client.setStatus(ClientStatus.ACTIVE);
        when(repo.findById(anyLong())).thenReturn(client);
        Response<Client> response = new Response<>();
        when(repo.update(anyLong(), any(Field.class))).thenReturn(response);
        Response<Client> result = service.blockClient(1L);

        verify(repo, times(1)).findById(anyLong());
        verify(repo, times(1)).update(anyLong(), any(Field.class));
        assertThat(result.getErrors(), empty());

    }

    @Test
    void blockClientAlreadyBlocked() throws SQLException {
        Client client = new Client();
        client.setStatus(ClientStatus.BLOCKED);
        when(repo.findById(anyLong())).thenReturn(client);

        Response<Client> result = service.blockClient(1L);

        verify(repo, times(1)).findById(anyLong());
        verify(repo, never()).update(anyLong(), any(Field.class));
        assertThat(result.getErrors().get(0), equalTo("Client already has status BLOCKED. Update not required"));
    }

    @Test
    void blockClientNotExists() throws SQLException {


        when(repo.findById(anyLong())).thenReturn(null);

        Response<Client> result = service.blockClient(1L);

        verify(repo, times(1)).findById(anyLong());
        verify(repo, never()).update(anyLong(), any(Field.class));
        assertThat(result.getErrors().get(0), equalTo("Client doesn't exist"));
    }

    @Test
    void blockClientException() throws SQLException {
        Client client = new Client();
        client.setStatus(ClientStatus.ACTIVE);
        when(repo.findById(anyLong())).thenReturn(client);

        when(repo.update(anyLong(), any(Field.class))).thenThrow(new SQLException());
        Response<Client> result = service.blockClient(1L);

        verify(repo, times(1)).findById(anyLong());
        verify(repo, times(1)).update(anyLong(), any(Field.class));
        assertThat(result.getErrors().get(0), equalTo("Error during client status changing clientId = 1"));

    }


    @Test
    void unblockClientSuccess() throws SQLException {
        Client client = new Client();
        client.setStatus(ClientStatus.BLOCKED);
        when(repo.findById(anyLong())).thenReturn(client);
        Response<Client> response = new Response<>();
        when(repo.update(anyLong(), any(Field.class))).thenReturn(response);
        Response<Client> result = service.unblockClient(1L);

        verify(repo, times(1)).findById(anyLong());
        verify(repo, times(1)).update(anyLong(), any(Field.class));
        assertThat(result.getErrors(), empty());

    }

    @Test
    void countSuccess() throws SQLException {

        service.count();
        verify(repo, times(1)).count();
    }

    @Test
    void countFail() throws SQLException {
        when(repo.count()).thenThrow(new SQLException());

        Exception exception = assertThrows(ServiceException.class, () ->
                service.count()
        );
        verify(repo, times(1)).count();
        assertTrue(exception.getMessage().contains("Error during getting client count"));
    }
}
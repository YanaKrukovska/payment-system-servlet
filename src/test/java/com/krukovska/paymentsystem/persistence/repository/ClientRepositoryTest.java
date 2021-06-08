package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.mapper.ClientMapper;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.ClientStatus;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static com.krukovska.paymentsystem.persistence.model.ClientStatus.ACTIVE;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ClientRepositoryTest extends AbstractDBTest {
    private final ClientRepository repository = new ClientRepository(DataSource.getInstance(), new ClientMapper());

    @Test
    void findById() throws SQLException {
        Client client = repository.findById(1L);

        assertThat(client.getId(), equalTo(1L));
        assertThat(client.getAccounts(), notNullValue());
        assertThat(client.getStatus(), equalTo(ACTIVE));
        assertThat(client.getUser().getId(), equalTo(2L));
    }

    @Test
    void updateStatus() throws SQLException {
        Client client = repository.findById(1L);
        ClientStatus newStatus = client.getStatus() == ACTIVE ? ClientStatus.BLOCKED : ACTIVE;

        repository.update(client.getId(), new Field("status", newStatus));

        Client updated = repository.findById(1L);

        assertThat(updated.getId(), equalTo(1L));
        assertThat(updated.getAccounts(), notNullValue());
        assertThat(updated.getStatus(), equalTo(newStatus));
        assertThat(updated.getUser().getId(), equalTo(2L));
    }


    @Test
    void getAll() throws SQLException {
        List<Client> result = repository.findAll(
                new PageAndSort(1, 5, PageAndSort.Direction.ASC, "id"));

        assertThat(result.stream().map(Client::getId).collect(toList()),
                contains(1L, 2L));
    }

    @Test
    void getTableName() {
        assertThat(repository.getTableName(), equalToIgnoringCase("clients"));
    }
}
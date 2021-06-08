package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.mapper.UnblockRequestMapper;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UnblockRequestRepositoryImplTest extends AbstractDBTest {

    private final UnblockRequestRepository repository = new UnblockRequestRepository(DataSource.getInstance(),
            new UnblockRequestMapper());

    @Test
    void createNewRequest() throws SQLException {
        UnblockRequest request = new UnblockRequest();
        Account account = new Account();
        account.setId(2L);
        request.setAccount(account);
        Client client = new Client();
        client.setId(2L);
        request.setClient(client);
        request.setCreationDate(LocalDate.now());

        UnblockRequest createdRequest = repository.create(request);
        assertThat(createdRequest.getId(), notNullValue());
    }


    @Test
    void getAllP1() throws SQLException {
        List<UnblockRequest> result = repository.findAll(
                new PageAndSort(1, 2, PageAndSort.Direction.ASC, "id"));

        assertThat(result.stream().map(UnblockRequest::getId).collect(toList()),
                contains(1L, 2L));
    }

    @Test
    void getAllP2() throws SQLException {
        List<UnblockRequest> result = repository.findAll(
                new PageAndSort(2, 2, PageAndSort.Direction.ASC, "id"));

        assertThat(result.stream().map(UnblockRequest::getId).collect(toList()),
                contains(3L, 4L));
    }

    @Test
    void countAll() throws SQLException {
        long result = repository.count();

        assertThat(result, equalTo(7L));

    }

    @Test
    void getTableName() {
        assertThat(repository.getTableName(), equalToIgnoringCase("unblock_requests"));
    }
}

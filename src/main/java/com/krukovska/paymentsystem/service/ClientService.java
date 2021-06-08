package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.Response;

import java.util.List;

public interface ClientService {

    /**
     * @param id id of client that needs to be found
     * @return found client
     */
    Client findClientById(long id);

    /**
     * @param page paging and sorting parameters for search
     * @return all clients
     */
    List<Client> findAllClients(PageAndSort page);

    /**
     * @param clientId id of client that needs to be blocked
     * @return updated client and list of error messages
     */
    Response<Client> blockClient(long clientId);

    /**
     * @param clientId id of client that needs to be unblocked
     * @return updated client and list of error messages
     */
    Response<Client> unblockClient(long clientId);

    /**
     * @return amount of all clients
     */
    long count();
}

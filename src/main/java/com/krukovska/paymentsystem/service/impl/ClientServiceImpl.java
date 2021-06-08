package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.ClientStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.Repository;
import com.krukovska.paymentsystem.service.ClientService;
import com.krukovska.paymentsystem.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ClientServiceImpl implements ClientService {
    private static final Logger logger = LogManager.getLogger(ClientServiceImpl.class);
    private final Repository<Client> clientRepository;

    public ClientServiceImpl(Repository<Client> clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client findClientById(long id) {
        logger.debug("Get client by id = {}", id);
        try {
            return clientRepository.findById(id);
        } catch (SQLException e) {
            String errorMessage = "Error during getting client with id=" + id;
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    @Override
    public List<Client> findAllClients(PageAndSort page) {
        logger.debug("Get all clients page = {}", page);
        try {
            return clientRepository.findAll(page);
        } catch (SQLException e) {
            String errorMessage = "Error during getting all clients for page " + page;
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    @Override
    public Response<Client> blockClient(long clientId) {
        logger.debug("Blocking Client id = {}", clientId);
        return changeClientStatus(clientId, ClientStatus.BLOCKED);
    }

    @Override
    public Response<Client> unblockClient(long clientId) {
        logger.debug("Unblocking Client id = {}", clientId);
        return changeClientStatus(clientId, ClientStatus.ACTIVE);
    }

    @Override
    public long count() {
        logger.debug(" Client count");
        try {
            return clientRepository.count();
        } catch (SQLException e) {
            String errorMessage = "Error during getting client count";
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }
    }


    private Response<Client> changeClientStatus(long clientId, ClientStatus newStatus) {
        logger.debug("Changing status for client  id = {} new status = {}", clientId, newStatus);

        Client client;
        try {
            client = clientRepository.findById(clientId);

            if (client == null) {
                return new Response<>(Collections.singletonList("Client doesn't exist"));
            }
            if (client.getStatus() != newStatus) {
                clientRepository.update(clientId, new Field("status", newStatus));
                logger.debug("client  status id = {} has been changed. New status = {}", clientId, newStatus);
                return new Response<>(client);
            } else {
                return new Response<>(client, "Client already has status " + newStatus + ". Update not required");
            }
        } catch (SQLException e) {
            String errorMessage = "Error during client status changing clientId = " + clientId;
            logger.error("errorMessage", e);
            return new Response<>(errorMessage);
        }
    }
}

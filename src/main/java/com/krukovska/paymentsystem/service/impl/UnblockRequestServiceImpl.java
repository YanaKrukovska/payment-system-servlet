package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;
import com.krukovska.paymentsystem.persistence.repository.Repository;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.ServiceException;
import com.krukovska.paymentsystem.service.UnblockRequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class UnblockRequestServiceImpl implements UnblockRequestService {

    private static final Logger logger = LogManager.getLogger(UnblockRequestServiceImpl.class);
    private final Repository<UnblockRequest> repository;
    private final AccountService accountService;

    public UnblockRequestServiceImpl(Repository<UnblockRequest> repository, AccountService accountService) {
        this.repository = repository;
        this.accountService = accountService;
    }

    @Override
    public UnblockRequest findRequestById(long requestId) {
        try {
            return repository.findById(requestId);
        } catch (SQLException e) {
            String errorMessage = "Error during getting request with id=" + requestId;
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }
    }

    @Override
    public Response<UnblockRequest> createNewRequest(long accountId, long clientId) {


        UnblockRequest request = new UnblockRequest();
        Account account = new Account();
        account.setId(accountId);
        request.setAccount(account);
        Client client = new Client();
        client.setId(clientId);
        request.setClient(client);
        request.setCreationDate(LocalDate.now());
        logger.debug("Creating new request {}", request);

        try {
            return new Response<>(repository.create(request));
        } catch (SQLException e) {
            String errorMessage = "Error during creating new request =" + request;
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }
    }

    @Override
    public List<UnblockRequest> findAllClientRequests(long clientId, PageAndSort page) {
        logger.debug("Find requests for clientId {}, page and sort {}", clientId, page);
        requireNonNull(page, "page must be not null");

        try {
            return repository.findByField(new Field("client_id", clientId), page);
        } catch (SQLException e) {
            String errorMessage = "Error during getting requests for client with id=" + clientId;
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }
    }

    @Override
    public Response<UnblockRequest> acceptRequest(long requestId) {

        UnblockRequest request = findRequestById(requestId);
        if (request == null) {
            String message = "Unblock request with id " + requestId + " does not exist";
            logger.error(message);
            return new Response<>(message);

        }
        accountService.unblockAccount(request.getAccount().getId());
        try {
            return repository.update(requestId, new Field("action_date", LocalDate.now()));
        } catch (SQLException e) {
            String errorMessage = "Error during updating request =" + request;
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }
    }

    @Override
    public List<UnblockRequest> findAllRequests(PageAndSort page) {
        logger.debug("findAllRequests with page and sort {}", page);
        requireNonNull(page, "Requests page must be not null");
        try {
            return repository.findAll(page);
        } catch (SQLException e) {
            String errorMessage = "Error during getting all requests";
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }
    }

    @Override
    public long countAll() {
        try {
            return repository.count();
        } catch (SQLException e) {
            String errorMessage = "Error during counting all requests";
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }
    }

}

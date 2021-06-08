package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.AccountRepository;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static com.krukovska.paymentsystem.persistence.model.AccountStatus.ACTIVE;
import static com.krukovska.paymentsystem.persistence.model.AccountStatus.BLOCKED;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * @param clientId id of client whose accounts need to be found
     * @param page     sorting and paging parameters that need to be applied =
     * @return all accounts of a client
     */
    @Override
    public List<Account> findAllClientAccounts(long clientId, PageAndSort page) {
        logger.debug("findAllClientAccounts for clientId {}, page and sort {}", clientId, page);
        requireNonNull(page, "Client page must be not null");

        try {
            return accountRepository.findByField(new Field("client_id", clientId), page);
        } catch (SQLException e) {
            String errorMessage = "Error during getting accounts for client with id=" + clientId;
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    /**
     * @param accountId id of account that needs to be found
     * @return found account
     */
    @Override
    public Account findAccountById(long accountId) {

        try {
            return accountRepository.findById(accountId);
        } catch (SQLException e) {
            String errorMessage = "Error during getting account with id=" + accountId;
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    /**
     * @param accountId id of account that needs to be topped up
     * @param amount    amount of money that should be added to the balance of account
     * @return response consisting of updated account and found errors
     */
    @Override
    public Response<Account> topUpAccount(long accountId, BigDecimal amount) {
        logger.debug("TopUp account with id {} sum {}", accountId, amount);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.debug("Sum must be > 0");
            return new Response<>(singletonList("You can't add less than 0"));
        }

        try {
            return accountRepository.updateBalance(accountId, amount);
        } catch (SQLException e) {
            String errorMessage = "Error during top up account with id=" + accountId;
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }

    }

    /**
     * @param accountId id of account that needs to be blocked
     * @return response consisting of updated account and found errors
     */
    @Override
    public Response<Account> blockAccount(long accountId) {
        try {
            return accountRepository.update(accountId, new Field("status", BLOCKED));
        } catch (SQLException e) {
            String message = "Error during blocking account " + accountId;
            logger.error(message, e);
            return new Response<>(Collections.singletonList(message));
        }
    }

    /**
     * @param accountId id of account that needs to be unblocked
     * @return response consisting of updated account and found errors
     */
    @Override
    public Response<Account> unblockAccount(long accountId) {
        try {
            return accountRepository.update(accountId, new Field("status", ACTIVE));
        } catch (SQLException e) {
            String message = "Error during unblocking account " + accountId;
            logger.error(message, e);
            return new Response<>(Collections.singletonList(message));
        }
    }

    /**
     * @param accountId id of account that needs to have money withdrawn
     * @param amount    response consisting of updated account and found errors
     * @return response consisting of updated account and found errors
     */
    @Override
    public Response<Account> withdrawAccount(long accountId, BigDecimal amount) {
        logger.debug("withdraw {} from account {}", amount, accountId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            String message = "Sum must be > 0. current amount =  " + amount;
            logger.debug(message);
            return new Response<>(singletonList(message));
        } else {
            try {
                return accountRepository.withdrawAccount(accountId, amount);
            } catch (SQLException e) {
                String message = "Error during withdraw account " + accountId + " with amount = " + amount;
                logger.error(message, e);
                return new Response<>(Collections.singletonList(message));
            }
        }

    }

    /**
     * @param clientId id of client whose accounts have to be counted
     * @return amount of client's accounts
     */
    @Override
    public long countByClientId(long clientId) {
        try {
            return accountRepository.countByField(new Field("client_id", clientId));
        } catch (SQLException e) {
            String errorMessage = "Error during counting accounts for clientId=" + clientId;
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }
    }
}

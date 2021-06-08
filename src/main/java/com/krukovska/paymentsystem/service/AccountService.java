package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    /**
     * @param clientId id of client whose accounts need to be found
     * @param page     sorting and paging parameters that need to be applied =
     * @return all accounts of a client
     */
    List<Account> findAllClientAccounts(long clientId, PageAndSort page);

    /**
     * @param accountId id of account that needs to be found
     * @return found account
     */
    Account findAccountById(long accountId);

    /**
     * @param accountId id of account that needs to be topped up
     * @param amount    amount of money that should be added to the balance of account
     * @return response consisting of updated account and found errors
     */
    Response<Account> topUpAccount(long accountId, BigDecimal amount);

    /**
     * @param accountId id of account that needs to be blocked
     * @return response consisting of updated account and found errors
     */
    Response<Account> blockAccount(long accountId);

    /**
     * @param accountId id of account that needs to be unblocked
     * @return response consisting of updated account and found errors
     */
    Response<Account> unblockAccount(long accountId);

    /**
     * @param accountId id of account that needs to have money withdrawn
     * @param amount    response consisting of updated account and found errors
     * @return response consisting of updated account and found errors
     */
    Response<Account> withdrawAccount(long accountId, BigDecimal amount);

    /**
     * @param clientId id of client whose accounts have to be counted
     * @return amount of client's accounts
     */
    long countByClientId(long clientId);
}

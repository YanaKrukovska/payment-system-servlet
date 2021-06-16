package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;

import java.util.List;

public interface UnblockRequestService {

    /**
     * finds request by given id
     *
     * @param requestId id of request that needs to be found
     * @return found unblock request
     */
    UnblockRequest findRequestById(long requestId);

    /**
     * creates and saves new unblocked request
     *
     * @param accountId id of account for which request is created
     * @param clientId  id of client for which request is created
     * @return created unblock request and list of error messages
     */
    Response<UnblockRequest> createNewRequest(long accountId, long clientId);

    /**
     * finds all unblock requests of certain client
     *
     * @param clientId id of client whose unblock requests need to be found
     * @param page     paging and sorting parameters for search
     * @return found unblock requests of certain client
     */
    List<UnblockRequest> findAllClientRequests(long clientId, PageAndSort page);

    /**
     * updates request
     *
     * @param requestId id of request that needs to be accepted
     * @return updated request and list of error messages
     */
    Response<UnblockRequest> acceptRequest(long requestId);

    /**
     * finds all requests
     *
     * @param page paging and sorting parameters for search
     * @return found requests
     */
    List<UnblockRequest> findAllRequests(PageAndSort page);

    /**
     * counts all unblock requests
     *
     * @return amount of unblock requests
     */
    long countAll();

}

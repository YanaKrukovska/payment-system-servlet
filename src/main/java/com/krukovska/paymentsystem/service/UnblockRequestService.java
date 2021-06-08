package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;

import java.util.List;

public interface UnblockRequestService {

    UnblockRequest findRequestById(long requestId);

    Response<UnblockRequest> createNewRequest(long accountId, long clientId);

    List<UnblockRequest> findAllClientRequests(long clientId, PageAndSort page);

    Response<UnblockRequest> acceptRequest(long requestId);

    List<UnblockRequest> findAllRequests(PageAndSort page);

    long countAll();

}

package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.mapper.AccountMapper;
import com.krukovska.paymentsystem.persistence.mapper.UnblockRequestMapper;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;
import com.krukovska.paymentsystem.persistence.repository.AccountRepositoryImpl;
import com.krukovska.paymentsystem.persistence.repository.UnblockRequestRepository;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.UnblockRequestService;
import com.krukovska.paymentsystem.service.impl.AccountServiceImpl;
import com.krukovska.paymentsystem.service.impl.UnblockRequestServiceImpl;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.krukovska.paymentsystem.util.AttributeHelper.setSortPaginationAttributes;
import static com.krukovska.paymentsystem.util.URLConstants.*;

@WebServlet(urlPatterns = {REQUEST_ALL, REQUEST_ADD, REQUEST_ACCEPT, REQUEST_DECLINE})
public class UnblockRequestServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(UnblockRequestServlet.class);

    private final AccountService accountService = new AccountServiceImpl(new AccountRepositoryImpl(DataSource.getInstance(),
            new AccountMapper()));
    private final UnblockRequestService requestService = new UnblockRequestServiceImpl(new
            UnblockRequestRepository(DataSource.getInstance(), new UnblockRequestMapper()), accountService);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageAndSort pageAndSort = PageAndSort.fromRequest(request);

        HttpSession httpSession = request.getSession();
        boolean isAdmin = (boolean) httpSession.getAttribute("isAdmin");
        long userId = (long) httpSession.getAttribute("userId");

        List<UnblockRequest> requests;
        if (isAdmin) {
            requests = requestService.findAllRequests(pageAndSort);
        } else {
            requests = requestService.findAllClientRequests(userId, pageAndSort);
        }

        for (UnblockRequest req : requests) {
            req.setAccount(accountService.findAccountById(req.getAccount().getId()));
        }

        request.setAttribute("requests", requests);
        request.setAttribute("requestPage", pageAndSort);

        setSortPaginationAttributes(request, requestService.countAll());
        request.getRequestDispatcher("requests.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case REQUEST_ADD:
                addRequest(request, response);
                break;
            case REQUEST_ACCEPT:
                updateRequest(request, response);
                break;
            case REQUEST_DECLINE:
                updateRequest(request, response);
                break;
            default:
                String errorMessage = "URL is not supported. url = " + action;
                logger.error(errorMessage);
                throw new ServletException(errorMessage);

        }
    }


    private void addRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String accountId = request.getParameter("accountId");
        if (!NumberUtils.isParsable(accountId)) {
            throw new ServletException("Wrong value of accountId");
        }

        HttpSession httpSession = request.getSession();
        long userId = (long) httpSession.getAttribute("userId");
        Response<UnblockRequest> createResponse = requestService.createNewRequest(Long.valueOf(accountId), userId);
        if (createResponse.hasErrors()) {
            request.setAttribute("errors", createResponse.getErrors());
        }
        response.sendRedirect(request.getContextPath() + ACCOUNT_ALL);
    }

    private void updateRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = request.getParameter("requestId");
        if (!NumberUtils.isParsable(requestId)) {
            throw new ServletException("Wrong value of requestId");
        }
        requestService.acceptRequest(Long.valueOf(requestId));
        response.sendRedirect(request.getContextPath() + REQUEST_ALL);

    }


}

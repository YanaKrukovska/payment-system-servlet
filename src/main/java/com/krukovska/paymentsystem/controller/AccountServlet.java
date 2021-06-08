package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.mapper.AccountMapper;
import com.krukovska.paymentsystem.persistence.mapper.CreditCardMapper;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.AccountRepositoryImpl;
import com.krukovska.paymentsystem.persistence.repository.CreditCardRepository;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.CreditCardService;
import com.krukovska.paymentsystem.service.impl.AccountServiceImpl;
import com.krukovska.paymentsystem.service.impl.CreditCardServiceImpl;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.krukovska.paymentsystem.util.AttributeHelper.setSortPaginationAttributes;
import static com.krukovska.paymentsystem.util.URLConstants.*;

@WebServlet(urlPatterns = {ACCOUNT_ALL, ACCOUNT_TOP_UP, ACCOUNT_BLOCK})
public class AccountServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(AccountServlet.class);
    private final AccountService accountService = new AccountServiceImpl(new AccountRepositoryImpl(DataSource.getInstance(),
            new AccountMapper()));
    private final CreditCardService cardService = new CreditCardServiceImpl(new CreditCardRepository(DataSource.getInstance(),
            new CreditCardMapper()));


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case ACCOUNT_ALL:
                getAllAccounts(request, response);
                break;
            case ACCOUNT_TOP_UP:
                getTopUpPage(request, response);
                break;
            default:
                String errorMessage = "URL is not supported. url = " + action;
                logger.error(errorMessage);
                throw new ServletException(errorMessage);

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case ACCOUNT_BLOCK:
                blockAccount(request, response);
                break;
            case ACCOUNT_TOP_UP:
                topUpAccount(request, response);
                break;
            default:
                String errorMessage = "URL is not supported. url = " + action;
                logger.error(errorMessage);
                throw new ServletException(errorMessage);

        }
    }

    private void getAllAccounts(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        PageAndSort pageAndSort = PageAndSort.fromRequest(request);
        HttpSession httpSession = request.getSession();
        long userId = (long) httpSession.getAttribute("userId");
        List<Account> accountList = accountService.findAllClientAccounts(userId, pageAndSort);

        for (Account account : accountList) {
            account.setCreditCard(cardService.findCreditCardByAccountId(account.getId()));
        }

        request.setAttribute("accountList", accountList);
        request.setAttribute("accountPage", pageAndSort);

        setSortPaginationAttributes(request, accountService.countByClientId(userId));
        request.getRequestDispatcher("accounts.jsp").forward(request, response);
    }

    private void getTopUpPage(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        if (!NumberUtils.isParsable(request.getParameter("accountId"))) {
            throw new ServletException("Wrong value of accountId");
        }

        long accountId = Long.parseLong(request.getParameter("accountId"));
        Account account = accountService.findAccountById(accountId);

        if (account == null) {
            request.setAttribute("message", "Account doesn't exist");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

        request.setAttribute("accountId", accountId);
        request.getRequestDispatcher("account-topup.jsp").forward(request, response);
    }

    private void blockAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accountIdParam = request.getParameter("accountId");
        if (!NumberUtils.isParsable(accountIdParam)) {
            logger.error("{} is not valid accountId", accountIdParam);
            throw new ServletException("Wrong value of accountId");
        }

        Response<Account> updateResponse = accountService.blockAccount(Long.parseLong(accountIdParam));
        if (updateResponse.hasErrors()) {
            request.setAttribute("message", updateResponse.getErrors().get(0));
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + ACCOUNT_ALL);
        }


    }

    private void topUpAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String accountIdParam = request.getParameter("accountId");
        if (!NumberUtils.isParsable(accountIdParam)) {
            throw new ServletException("Wrong value of accountId:" + accountIdParam);
        }

        String amount = request.getParameter("amount");
        if (!NumberUtils.isParsable(amount)) {
            throw new ServletException("Wrong value of amount:" + amount);
        }


        List<String> errors = new ArrayList<>();
        Response<Account>
                updateResponse = accountService.topUpAccount(Long.parseLong(accountIdParam), new BigDecimal(amount));
        if (updateResponse.hasErrors()) {
            errors.addAll(updateResponse.getErrors());
        }


        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/account-all");
        } else {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("account-topup.jsp").forward(request, response);
        }
    }

}

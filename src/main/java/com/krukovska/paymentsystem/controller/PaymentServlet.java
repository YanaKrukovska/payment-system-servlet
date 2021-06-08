package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.mapper.AccountMapper;
import com.krukovska.paymentsystem.persistence.mapper.PaymentMapper;
import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.PaymentStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.AccountRepositoryImpl;
import com.krukovska.paymentsystem.persistence.repository.PaymentRepositoryImpl;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.PaymentService;
import com.krukovska.paymentsystem.service.impl.AccountServiceImpl;
import com.krukovska.paymentsystem.service.impl.PaymentServiceImpl;
import org.apache.commons.lang3.StringUtils;
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
import java.time.LocalDate;
import java.util.List;

import static com.krukovska.paymentsystem.util.AttributeHelper.setSortPaginationAttributes;
import static com.krukovska.paymentsystem.util.URLConstants.*;

@WebServlet(urlPatterns = {PAYMENT_ALL, PAYMENT_ADD, PAYMENT_SEND})
public class PaymentServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(PaymentServlet.class);
    private final PaymentService paymentService = new PaymentServiceImpl(new PaymentRepositoryImpl(DataSource.getInstance(), new PaymentMapper()));
    private final AccountService accountService = new AccountServiceImpl(new AccountRepositoryImpl(DataSource.getInstance(), new AccountMapper()));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case PAYMENT_ALL:
                getAllPayments(request, response);
                break;
            case PAYMENT_ADD:
                getAddPaymentPage(request, response);
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
            case PAYMENT_ADD:
                createPayment(request, response);
                break;
            case PAYMENT_SEND:
                sendPayment(request, response);
                break;
            default:
                String errorMessage = "URL is not supported. url = " + action;
                logger.error(errorMessage);
                throw new ServletException(errorMessage);
        }
    }

    private void sendPayment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String paymentId = request.getParameter("paymentId");
        if (!NumberUtils.isParsable(paymentId)) {
            logger.error("{} is not valid paymentId", paymentId);
            throw new ServletException("Wrong value of paymentId");
        }

        Response<Payment> sendResponse = paymentService.send(Long.parseLong(paymentId));
        if (sendResponse.isOkay()) {
            response.sendRedirect(request.getContextPath() + ACCOUNT_ALL);
        } else {
            request.setAttribute("errors", sendResponse.getErrors());
            request.getRequestDispatcher("payments.jsp").forward(request, response);
        }
    }

    private void createPayment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountId = request.getParameter("accountId");
        if (!NumberUtils.isParsable(accountId)) {
            logger.error("{} is not valid accountId", accountId);
            throw new ServletException("Wrong value of accountId");
        }

        String receiverIban = request.getParameter("receiverIban");
        if (StringUtils.isEmpty(receiverIban)) {
            logger.error("{} is not valid receiverIban", receiverIban);
            throw new ServletException("Wrong value of receiverIban");
        }

        String amount = request.getParameter("amount");
        if (!NumberUtils.isParsable(amount)) {
            logger.error("{} is not valid amount", amount);
            throw new ServletException("Wrong value of amount");
        }

        Account account = accountService.findAccountById(Long.parseLong(accountId));
        if (account == null) {
            logger.error("Account with id {} doesn't exist", accountId);
            throw new ServletException("No such account");
        }

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.CREATED);
        payment.setAccount(account);
        payment.setAmount(BigDecimal.valueOf(Double.parseDouble(amount)));
        payment.setReceiverIban(receiverIban);
        payment.setPaymentDate(LocalDate.now());

        String details = request.getParameter("details");
        if (details != null) {
            payment.setDetails(details);
        }

        paymentService.create(payment);
        response.sendRedirect(request.getContextPath() + PAYMENT_ALL);
    }

    private void getAllPayments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageAndSort pageAndSort = PageAndSort.fromRequest(request);
        HttpSession httpSession = request.getSession();
        long userId = (long) httpSession.getAttribute("userId");

        List<Payment> paymentList = paymentService.findAllClientPayments(userId, pageAndSort);
        request.setAttribute("payments", paymentList);
        request.setAttribute("payPage", pageAndSort);

        for (Payment payment : paymentList) {
            payment.setAccount(accountService.findAccountById(payment.getAccount().getId()));
        }

        setSortPaginationAttributes(request, paymentService.count(userId));
        request.getRequestDispatcher("payments.jsp").forward(request, response);
    }

    private void getAddPaymentPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accountId = request.getParameter("accountId");
        if (!NumberUtils.isParsable(accountId)) {
            logger.error("{} is not valid accountId", accountId);
            throw new ServletException("Wrong value of accountId");
        }

        Account account = accountService.findAccountById(Long.parseLong(accountId));
        if (account == null) {
            logger.error("Account with id {} doesn't exist", accountId);
            throw new ServletException("No such account");
        }


        request.setAttribute("accountId", accountId);
        request.setAttribute("balance", account.getBalance());
        request.getRequestDispatcher("payment-create.jsp").forward(request, response);

    }

}

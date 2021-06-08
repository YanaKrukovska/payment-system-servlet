package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.mapper.CreditCardMapper;
import com.krukovska.paymentsystem.persistence.model.CreditCard;
import com.krukovska.paymentsystem.persistence.repository.CreditCardRepository;
import com.krukovska.paymentsystem.service.CreditCardService;
import com.krukovska.paymentsystem.service.impl.CreditCardServiceImpl;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.krukovska.paymentsystem.util.URLConstants.CARD;

@WebServlet(urlPatterns = {CARD})
public class CardServlet extends HttpServlet {

    private final CreditCardService cardService = new CreditCardServiceImpl(new CreditCardRepository(DataSource.getInstance(),
            new CreditCardMapper()));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cardNumber = request.getParameter("cardNumber");

        if (StringUtils.isEmpty(cardNumber)) {
            request.setAttribute("message", "Card number can't be empty");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

        CreditCard creditCard = cardService.findCardByCardNumber(cardNumber);
        if (creditCard == null) {
            request.setAttribute("message", "Card doesn't exist");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

        request.setAttribute("card", creditCard);
        request.getRequestDispatcher("card.jsp").forward(request, response);
    }

}

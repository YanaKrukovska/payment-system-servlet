package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.PageAndSort;
import com.krukovska.paymentsystem.persistence.mapper.ClientMapper;
import com.krukovska.paymentsystem.persistence.mapper.UserMapper;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.repository.ClientRepository;
import com.krukovska.paymentsystem.persistence.repository.UserRepository;
import com.krukovska.paymentsystem.service.ClientService;
import com.krukovska.paymentsystem.service.UserService;
import com.krukovska.paymentsystem.service.impl.ClientServiceImpl;
import com.krukovska.paymentsystem.service.impl.UserServiceImpl;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.krukovska.paymentsystem.util.AttributeHelper.setSortPaginationAttributes;
import static com.krukovska.paymentsystem.util.URLConstants.*;

@WebServlet(urlPatterns = {CLIENT_ALL, CLIENT_BLOCK, CLIENT_UNBLOCK})
public class ClientServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ClientServlet.class);
    private final ClientService clientService = new ClientServiceImpl(new ClientRepository(DataSource.getInstance(), new ClientMapper()));
    private final UserService userService = new UserServiceImpl(new UserRepository(DataSource.getInstance(), new UserMapper()));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageAndSort pageAndSort = PageAndSort.fromRequest(request);

        List<Client> clientList = clientService.findAllClients(pageAndSort);

        for (Client client : clientList) {
            client.setUser(userService.getById(client.getId()));
        }

        request.setAttribute("clientList", clientList);

        setSortPaginationAttributes(request, clientService.count());
        request.getRequestDispatcher("clients.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case CLIENT_BLOCK:
                blockClient(request, response);
                break;
            case CLIENT_UNBLOCK:
                unblockClient(request, response);
                break;
            default:
                String errorMessage = "URL is not supported. url = " + action;
                logger.error(errorMessage);
                throw new ServletException(errorMessage);

        }
    }

    private void blockClient(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String clientId = request.getParameter("clientId");
        if (!NumberUtils.isParsable(clientId)) {
            throw new ServletException("Wrong value of clientId");
        }

        clientService.blockClient(Long.parseLong(clientId));
        response.sendRedirect(request.getContextPath() + CLIENT_ALL);
    }

    private void unblockClient(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String clientId = request.getParameter("clientId");
        if (!NumberUtils.isParsable(clientId)) {
            throw new ServletException("Wrong value of clientId");
        }

        clientService.unblockClient(Long.parseLong(clientId));
        response.sendRedirect(request.getContextPath() + CLIENT_ALL);
    }

}

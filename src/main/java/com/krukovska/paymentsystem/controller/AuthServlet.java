package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.mapper.UserMapper;
import com.krukovska.paymentsystem.persistence.model.User;
import com.krukovska.paymentsystem.persistence.repository.UserRepository;
import com.krukovska.paymentsystem.service.UserService;
import com.krukovska.paymentsystem.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;

import static com.krukovska.paymentsystem.util.URLConstants.*;

@WebServlet(urlPatterns = {LOGIN, LOGOUT, WELCOME})
public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getServletPath();
        switch (action) {
            case LOGIN:
                getLoginPage(request, response);
                break;
            case LOGOUT:
                doLogOut(request, response);
                break;
            case WELCOME:
            default:
                getMainPage(request, response);
        }
    }

    private void getMainPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void getLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserService userService = new UserServiceImpl(new UserRepository(DataSource.getInstance(), new UserMapper()));
        User foundUser = userService.getByEmail(email);

        if (foundUser == null) {
            request.setAttribute("errors", Collections.singletonList("User not found"));
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else if (StringUtils.equals(foundUser.getPassword(), password)) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", foundUser.getId());
            session.setAttribute("userEmail", email);
            session.setAttribute("isAdmin", foundUser.getAdmin());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            request.setAttribute("errors", Collections.singletonList("Incorrect password"));
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }


    private void doLogOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        if (session != null) {
            session.removeAttribute("userEmail");
            session.removeAttribute("userId");
            session.removeAttribute("isAdmin");
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/login");
    }

}

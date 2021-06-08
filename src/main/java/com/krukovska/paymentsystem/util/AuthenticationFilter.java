package com.krukovska.paymentsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

import static com.krukovska.paymentsystem.util.URLConstants.ADMIN_ROLE_REQUIRED_URLS;
import static com.krukovska.paymentsystem.util.URLConstants.LOGIN_REQUIRED_URLS;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(AuthenticationFilter.class);
    private HttpServletRequest httpRequest;
    private HttpSession session;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        httpRequest = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        session = httpRequest.getSession(false);

        setReturnPreventionParams(res);

        String requestPath = httpRequest.getServletPath();

        if (isAdminRoleRequired() && !isUserAdmin()) {
            logger.debug("not admin user couldn't access {}, redirecting to welcome page", requestPath);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        } else if (isLoginRequired() && !isUserLoggedIn()) {
            logger.debug("not logged in user couldn't access {}, redirecting to login page", requestPath);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        } else {
            logger.info("user accessed path {}", requestPath);
            chain.doFilter(request, response);
        }
    }

    /**
     * checks if user is logged in
     *
     * @return true if user is logged in
     */
    private boolean isUserLoggedIn() {
        return session != null && session.getAttribute("userEmail") != null;
    }

    /**
     * checks if user is logged in and has admin role
     *
     * @return true if user is admin
     */
    private boolean isUserAdmin() {
        return isUserLoggedIn() && (Boolean) session.getAttribute("isAdmin");
    }

    /**
     * @return true if current path is among those who need user to be logged in
     */
    private boolean isLoginRequired() {
        String requestURL = httpRequest.getServletPath();
        return Arrays.stream(LOGIN_REQUIRED_URLS).anyMatch(requestURL::contains);
    }

    /**
     * @return true if current path is among those who need user to have admin role
     */
    private boolean isAdminRoleRequired() {
        String requestURL = httpRequest.getServletPath();
        return Arrays.stream(ADMIN_ROLE_REQUIRED_URLS).anyMatch(requestURL::contains);
    }

    /**
     * sets header params that prevent user from accessing pages that need user to be logged in in order to access the
     * page after user logged out
     *
     * @param res response where params will be set
     */
    private void setReturnPreventionParams(HttpServletResponse res) {
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // Default implementation
    }

    @Override
    public void destroy() {
        // Default implementation
    }
}

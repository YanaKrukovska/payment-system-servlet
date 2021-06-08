<!DOCTYPE HTML>
<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static com.krukovska.paymentsystem.util.URLConstants.*" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<html lang="${sessionScope.lang}">
<head><title></title></head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<% String context = request.getContextPath(); %>

<nav class="navbar navbar-expand-sm navbar-dark bg-dark">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="<%=context%><%=WELCOME%>">
                <fmt:message key="navbar.navbarHeader"/>
            </a>
        </div>
        <ul class="navbar-nav mr-auto">
            <c:if test="${sessionScope.userEmail != null && !sessionScope.isAdmin}">
                <li class="nav-item">
                    <a class="nav-link" href="<%=context%><%=ACCOUNT_ALL%>">
                        <fmt:message key="navbar.accounts"/>
                    </a>
                </li>
            </c:if>
            <c:if test="${sessionScope.userEmail != null && !sessionScope.isAdmin}">
                <li class="nav-item">
                    <a class="nav-link" href="<%=context%><%=PAYMENT_ALL%>">
                        <fmt:message key="navbar.payments"/>
                    </a>
                </li>
            </c:if>
            <c:if test="${sessionScope.userEmail != null}">
                <li class="nav-item">
                    <a class="nav-link" href="<%=context%><%=REQUEST_ALL%>">
                        <fmt:message key="navbar.requests"/>
                    </a>
                </li>
            </c:if>
            <c:if test="${sessionScope.userEmail != null && sessionScope.isAdmin}">
                <li class="nav-item">
                    <a class="nav-link" href="<%=context%><%=CLIENT_ALL%>">
                        <fmt:message key="navbar.clients"/>
                    </a>
                </li>
            </c:if>
        </ul>
        <ul class="nav navbar-nav navbar-right">

            <c:if test="${sessionScope.userEmail == null}">
                <li>
                    <a class="nav-link" href="<%=context%>/login">
                        <fmt:message key="navbar.login"/>
                    </a>
                </li>
            </c:if>
            <c:if test="${sessionScope.userEmail != null}">
                <li>
                    <a class="nav-link" href="<%=context%>/logout">
                        <fmt:message key="navbar.logout"/>
                    </a>
                </li>
            </c:if>

            <% Object uri = request.getAttribute("javax.servlet.forward.request_uri"); %>
            <li>
                <a id="en-link" class="nav-link"
                   href="<%= uri %>?lang=en">ENG</a>
            </li>
            <li>
                <a id="ua-link" class="nav-link"
                   href="<%= uri %>?lang=ua">UA</a>
            </li>
        </ul>
    </div>
</nav>
</html>
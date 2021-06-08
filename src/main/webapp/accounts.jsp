<%@ page import="static com.krukovska.paymentsystem.util.URLConstants.ACCOUNT_ALL" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cardformat" uri="/WEB-INF/CardFormatterDescriptor.tld" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${param.lang}">
<head>
    <title><fmt:message key="navbar.accounts"/></title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<%@ include file="fragments/navbar.jsp" %>

<c:if test="${errors != null}">
    <c:forEach items="${errors}" var="error">
        <div class="alert alert-danger" role="alert">${error}</div>
    </c:forEach>
</c:if>

<table class="table">
    <thead class="thead-light">
    <tr>
        <th scope="col">
            <a href="<%=context%><%=ACCOUNT_ALL%>?size=${accountPage.size}&page=${page}&sortField=id&sortDir=${reverseSortDir}">
                <fmt:message key="general.number"/>
            </a>
        </th>
        <th scope="col">
            <a href="<%=context%><%=ACCOUNT_ALL%>?size=${accountPage.size}&page=${page}&sortField=name&sortDir=${reverseSortDir}">
                <fmt:message key="general.name"/>
            </a>
        </th>
        <th scope="col">
            <a href="<%=context%><%=ACCOUNT_ALL%>?size=${accountPage.size}&page=${page}&sortField=balance&sortDir=${reverseSortDir}">
                <fmt:message key="account.balance"/>
            </a>
        </th>
        <th scope="col">IBAN</th>
        <th scope="col">
            <fmt:message key="general.status"/>
        </th>
        <th scope="col">
            <fmt:message key="card.creditCard"/>
        </th>
        <th scope="col"></th>
        <th scope="col"></th>
        <th scope="col"></th>
        <th scope="col"></th>
    </tr>
    </thead>
    <tbody>

    <c:forEach items="${accountList}" var="account">
        <tr>
            <td class="align-middle"><span class="align-middle">${account.id}</span></td>
            <td class="align-middle"><span class="align-middle">${account.name}</span></td>
            <td class="align-middle"><span class="align-middle">${account.balance}</span></td>
            <td class="align-middle"><span class="align-middle">${account.iban}</span></td>
            <td class="align-middle"><span class="align-middle">${account.status}</span></td>
            <td class="align-middle">
                <a class="align-middle" href="<%=context%><%=CARD%>?cardNumber=${account.creditCard.cardNumber}">
                    <cardformat:card cardNumber="${account.creditCard.cardNumber}">
                    </cardformat:card>
                </a>
            </td>
            <td>
                <c:if test="${account.status == 'ACTIVE'}">
                    <form action="<%=context%><%=ACCOUNT_TOP_UP%>" method="get">
                        <label><input value="${account.id}" name="accountId" hidden></label>
                        <button type="submit" class="btn btn-success">
                            <fmt:message key="account.topUp"/>
                        </button>
                    </form>
                </c:if>
            </td>
            <td>
                <c:if test="${account.status == 'ACTIVE'}">
                    <form action="<%=context%><%=PAYMENT_ADD%>" method="get">
                        <input type="hidden" name="accountId" value="${account.id}">
                        <button type="submit" class="btn btn-primary">
                            <fmt:message key="payment.create"/>
                        </button>
                    </form>
                </c:if>
            </td>
            <td>
                <c:if test="${account.status == 'ACTIVE'}">
                    <form action="<%=context%><%=ACCOUNT_BLOCK%>?accountId=${account.id}" method="post">
                        <button type="submit" class="btn btn-danger">
                            <fmt:message key="general.block"/>
                        </button>
                    </form>
                </c:if>
            </td>
            <td>
                <c:if test="${account.status == 'BLOCKED'}">
                    <form action="<%=context%><%=REQUEST_ADD%>?accountId=${account.id}" method="post">
                        <button type="submit" class="btn btn-warning">
                            <fmt:message key="general.unblock"/>
                        </button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h:pagination pageNumbers="${pageNumbers}" path="<%=ACCOUNT_ALL%>"
    sortDir="${sortDir}" sortField="${sortField}" size="${size}" />

</body>
</html>

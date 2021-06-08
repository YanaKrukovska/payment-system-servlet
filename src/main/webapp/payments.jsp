<%@ page import="static com.krukovska.paymentsystem.util.URLConstants.PAYMENT_ALL" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="cardformat" uri="/WEB-INF/CardFormatterDescriptor.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="navbar.payments"/></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>

<%@ include file="fragments/navbar.jsp" %>

<table class="table">
    <thead class="thead-light">
    <tr>
        <th scope="col">
            <a href="<%=context%><%=PAYMENT_ALL%>?size=${payPage.size}&page=${page}&sortField=id&sortDir=${reverseSortDir}"
            ><fmt:message key="general.number"/>
            </a>
        </th>
        <th scope="col"><fmt:message key="general.amount"/></th>
        <th scope="col">
            <a href="<%=context%><%=PAYMENT_ALL%>?size=${payPage.size}&page=${page}&sortField=payment_date&sortDir=${reverseSortDir}"
            ><fmt:message key="payment.paymentDate"/>
            </a>
        </th>
        <th scope="col"><fmt:message key="payment.receiverIban"/></th>
        <th scope="col"><fmt:message key="general.status"/></th>
        <th scope="col"><fmt:message key="payment.details"/></th>
        <th scope="col"><fmt:message key="account.id"/></th>
        <th scope="col"><fmt:message key="account.name"/></th>
        <th scope="col"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${payments}" var="payment">
        <tr>
            <td class="align-middle"><span class="align-middle">${payment.id}</span></td>
            <td class="align-middle"><span class="align-middle">${payment.amount}</span></td>
            <td class="align-middle"><span class="align-middle">${payment.paymentDate}</span></td>
            <td class="align-middle"><span class="align-middle">${payment.receiverIban}</span></td>
            <td class="align-middle"><span class="align-middle">${payment.status}</span></td>
            <td class="align-middle"><span class="align-middle">${payment.details}</span></td>
            <td class="align-middle"><span class="align-middle">${payment.account.id}</span></td>
            <td class="align-middle"><span class="align-middle">${payment.account.name}</span></td>
            <td>
                <c:if test="${payment.status == 'CREATED'}">
                    <form action="<%=context%><%=PAYMENT_SEND%>" method="post">
                        <label><input value="${payment.id}" name="paymentId" hidden></label>
                        <button type="submit" class="btn btn-success">
                            <fmt:message key="payment.send"/>
                        </button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h:pagination pageNumbers="${pageNumbers}" path="<%=PAYMENT_ALL%>"
              sortDir="${sortDir}" sortField="${sortField}" size="${size}"/>

<c:if test="${errors != null}">
    <c:forEach items="${errors}" var="error">
        <div class="alert alert-danger" role="alert">${error}</div>
    </c:forEach>
</c:if>

</body>
</html>
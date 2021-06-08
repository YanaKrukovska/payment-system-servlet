<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="payment.create"/></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="static/css/form.css" rel="stylesheet"/>
</head>
<body>

<%@ include file="fragments/navbar.jsp" %>

<div class="custom-form">

    <form action="<%=context%><%=PAYMENT_ADD%>" method="post">

        <h2 class="text-center">
            <fmt:message key="payment.create"/>
        </h2>

        <div class="form-group">
            <label for="amount">
                <fmt:message key="general.amount"/>
            </label>
            <input id="amount" type="number" step="0.01" min="0.01" max="${balance}" class="form-control" name="amount"
                   required>
        </div>

        <div class="form-group">
            <label for="details">
                <fmt:message key="payment.details"/>
            </label>
            <input id="details" type="text" class="form-control" name="details">
        </div>

        <div class="form-group">
            <label for="receiverIban">
                <fmt:message key="payment.receiverIban"/>
            </label>
            <input id="receiverIban" type="text" class="form-control" name="receiverIban">
        </div>

        <label>
            <input value="${accountId}" name="accountId" hidden>
        </label>

        <div class="form-group">
            <button type="submit" class="btn btn-success btn-block">Create</button>
        </div>
    </form>
    <c:if test="${errors != null}">
        <c:forEach items="${errors}" var="error">
            <div class="alert alert-danger" role="alert">${error}</div>
        </c:forEach>
    </c:if>
</div>
</body>
</html>
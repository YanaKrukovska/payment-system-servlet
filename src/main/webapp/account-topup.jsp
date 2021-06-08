<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="account.topUpAccount"/></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="static/css/form.css" rel="stylesheet"/>
</head>
<body>

<%@ include file="fragments/navbar.jsp" %>

<div class="custom-form">

    <form action="<%=context%><%=ACCOUNT_TOP_UP%>" method="post">

        <h2 class="text-center">
            <fmt:message key="account.topUpAccount"/>
        </h2>

        <div class="form-group">
            <label for="amount">
                <fmt:message key="general.amount"/>
            </label>
            <input id="amount" type="number" step="0.01" min="0.01" class="form-control" name="amount" required>
        </div>

        <label>
            <input value="${accountId}" name="accountId" hidden>
        </label>

        <div class="form-group">
            <button type="submit" class="btn btn-success btn-block"><fmt:message key="account.topUp"/></button>
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
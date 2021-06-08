<!DOCTYPE html>
<%@taglib prefix="cardformat" uri="/WEB-INF/CardFormatterDescriptor.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<html lang="${sessionScope.lang}" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="navbar.card"/></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <style>
        .info {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 10%;
        }
    </style>
</head>
<body>
<%@ include file="fragments/navbar.jsp" %>
<div class="info">
    <div class="card" style="width: 18rem;">
        <div class="card-body">
            <h5 class="card-title">
                <cardformat:card cardNumber="${card.cardNumber}">
                </cardformat:card>
            </h5>
            <h6 class="card-subtitle mb-2 text-muted">
                <fmt:message key="card.isExpired"/>: ${card.isExpired()}
            </h6>

        </div>
    </div>
</div>
</body>
</html>
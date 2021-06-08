<%@ page import="static com.krukovska.paymentsystem.util.URLConstants.ACCOUNT_ALL" %>
<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>Oops</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="static/css/error.css" rel="stylesheet"/>
</head>
<body>

<div>
    <img src="https://pbs.twimg.com/media/EMX9ColXYAAW438.jpg" width="400" height="350" alt="cursed-emoji">
    <form class="info" action="<%=request.getContextPath()%><%=ACCOUNT_ALL%>" method="get">
        <h1 class="h3 mb-3 font-weight-normal" id="error-banner">
            <fmt:message key="error.wentWrong"/>
        </h1>

        <span class="text-info">${message}</span>
        <button type="submit" class="btn btn-primary">
            <fmt:message key="error.toMain"/>
        </button>
    </form>
</div>
</body>
</html>
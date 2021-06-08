<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <title><fmt:message key="navbar.login"/></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="static/css/form.css" rel="stylesheet" type="text/css" media="all"/>
</head>
<%@ include file="fragments/navbar.jsp" %>
<body>
<div class="custom-form">
    <form action="<%=request.getContextPath()%>/login" method="post">
        <h2 class="text-center">Log in</h2>
        <div class="form-group">
            <label for="email" hidden></label>
            <input id="email" type="email" class="form-control" placeholder="Email" name="email" required>
        </div>
        <div class="form-group">
            <label for="password" hidden></label>
            <input id="password" type="password" class="form-control" placeholder="Password" name="password"
                   required>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block">Log in</button>
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
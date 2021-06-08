<%@ page import="static com.krukovska.paymentsystem.util.URLConstants.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="navbar.clients"/></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>

<%@ include file="fragments/navbar.jsp" %>

<table class="table">
    <thead class="thead-light">
    <tr>
        <th scope="col">
            <fmt:message key="general.number"/>
        </th>
        <th scope="col">
            <fmt:message key="general.name"/>
        </th>
        <th scope="col">Email</th>
        <th scope="col">
            <fmt:message key="general.status"/>
        </th>
        <th scope="col"></th>
        <th scope="col"></th>
    </tr>
    </thead>
    <tbody>

    <c:forEach items="${clientList}" var="client">
        <tr>
            <td class="align-middle"><span class="align-middle">${client.id}</span></td>
            <td class="align-middle"><span class="align-middle">${client.user.name}</span></td>
            <td class="align-middle"><span class="align-middle">${client.user.email}</span></td>
            <td class="align-middle"><span class="align-middle">${client.status}</span></td>
            <td>
                <c:if test="${client.status == 'ACTIVE'}">
                    <form action="<%=context%><%=CLIENT_BLOCK%>?clientId=${client.id}" method="post">
                        <button type="submit" class="btn btn-danger">
                            <fmt:message key="general.block"/>
                        </button>
                    </form>
                </c:if>
            </td>
            <td>
                <c:if test="${client.status == 'BLOCKED'}">
                    <form action="<%=context%><%=CLIENT_UNBLOCK%>?clientId=${client.id}" method="post">
                        <button type="submit" class="btn btn-dark">
                            <fmt:message key="general.unblock"/>
                        </button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h:pagination pageNumbers="${pageNumbers}" path="<%=CLIENT_ALL%>"
              sortDir="${sortDir}" sortField="${sortField}" size="${size}" />

</body>
</html>
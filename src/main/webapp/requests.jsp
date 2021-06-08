<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="navbar.requests"/></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>

<%@ include file="fragments/navbar.jsp" %>

<table class="table">
    <thead class="thead-light">
    <tr>
        <th scope="col"><fmt:message key="general.number"/></th>
        <th scope="col"><fmt:message key="request.clientId"/></th>
        <th scope="col"><fmt:message key="account.id"/></th>
        <th scope="col"><fmt:message key="account.name"/></th>
        <th scope="col"><fmt:message key="request.creationDate"/></th>
        <th scope="col"><fmt:message key="request.actionDate"/></th>
        <th scope="col"></th>
        <th scope="col"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${requests}" var="request">
        <tr>
            <td class="align-middle"><span class="align-middle">${request.id}</span></td>
            <td class="align-middle"><span class="align-middle">${request.client.id}</span></td>
            <td class="align-middle"><span class="align-middle">${request.account.id}</span></td>
            <td class="align-middle"><span class="align-middle">${request.account.name}</span></td>
            <td class="align-middle"><span class="align-middle">${request.creationDate}</span></td>
            <td class="align-middle">
                <c:if test="${request.actionDate != null}">
                    <span class="align-middle">${request.actionDate}</span>
                </c:if>
            </td>
            <td>
                <c:if test="${request.actionDate == null && sessionScope.isAdmin}">
                    <form action="<%=context%><%=REQUEST_ACCEPT%>" method="post">
                        <label for="requestId" hidden></label>
                        <input id="requestId" type="number" name="requestId" value="${request.id}" hidden>
                        <button type="submit" class="btn btn-success">
                            <fmt:message key="request.accept"/>
                        </button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h:pagination pageNumbers="${pageNumbers}" path="<%=REQUEST_ALL%>"
              sortDir="${sortDir}" sortField="${sortField}" size="${size}" />

</body>
</html>
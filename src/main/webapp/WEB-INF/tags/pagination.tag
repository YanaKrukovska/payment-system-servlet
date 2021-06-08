<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pageNumbers" required="true" type="java.util.List" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="size" required="true" %>
<%@ attribute name="sortField" required="true" %>
<%@ attribute name="sortDir" required="true" rtexprvalue="true" %>
<%@ tag isELIgnored="false" %>
<% String context = request.getContextPath(); %>

<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <nav aria-label="Pagination">
            <ul class="pagination justify-content-center">
                <c:forEach items="${pageNumbers}" var="pageNumber">
                    <li class="page-item">
                        <a class="page-link"
                           href="<%=context%>${path}?size=${size}&page=${pageNumber}&sortField=${sortField}&sortDir=${sortDir}">
                                ${pageNumber}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </div>
    <div class="col-md-2"></div>
</div>
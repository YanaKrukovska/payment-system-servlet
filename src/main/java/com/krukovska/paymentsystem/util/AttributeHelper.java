package com.krukovska.paymentsystem.util;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.krukovska.paymentsystem.util.Constants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AttributeHelper {

    private AttributeHelper() {
    }

    /**
     * sets attributes for sorting
     *
     * @param request     request where attributes will be set
     * @param totalAmount total amount of elements found
     */
    public static void setSortPaginationAttributes(HttpServletRequest request, long totalAmount) {

        String sortDir = request.getParameter("sortDir");
        request.setAttribute("sortDir", isEmpty(sortDir) ? DEFAULT_SORTING_ORDER : sortDir);

        if (isEmpty(sortDir)) {
            request.setAttribute("reverseSortDir", DEFAULT_REVERSE_SORTING_ORDER);
        } else {
            request.setAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        }

        String sortField = request.getParameter("sortField");
        request.setAttribute("sortField", isEmpty(sortField) ? DEFAULT_SORTING_FIELD : sortField);

        setPaginationAttributes(request, totalAmount);
    }

    /**
     * sets attributes for pagination
     *
     * @param request     request where attributes will be set
     * @param totalAmount total amount of elements found
     */
    private static void setPaginationAttributes(HttpServletRequest request, long totalAmount) {
        String page = request.getParameter("page");
        request.setAttribute("page", isEmpty(page) ? DEFAULT_CURRENT_PAGE : page);

        String size = request.getParameter("size");
        request.setAttribute("size", isEmpty(size) ? DEFAULT_PAGE_SIZE : size);

        long pageSize = isEmpty(size) ? DEFAULT_PAGE_SIZE : Long.parseLong(size);

        long totalPages = (totalAmount + pageSize - 1) / pageSize;

        if (totalPages > 0) {
            request.setAttribute("pageNumbers", LongStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList()));
        }
    }
}

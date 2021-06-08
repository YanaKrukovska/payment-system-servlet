package com.krukovska.paymentsystem.persistence;


import javax.servlet.http.HttpServletRequest;

import static com.krukovska.paymentsystem.persistence.PageAndSort.Direction.ASC;
import static com.krukovska.paymentsystem.util.Constants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class PageAndSort {


    private int page;
    private int size;
    private PageAndSort.Direction direction;
    private String property;

    public PageAndSort(int page, int size, Direction direction, String property) {
        this.page = page;
        this.size = size;
        this.direction = direction;
        this.property = property;
    }

    public PageAndSort() {
        this.direction = ASC;
        this.property = DEFAULT_SORTING_FIELD;
        this.page = DEFAULT_CURRENT_PAGE;
        this.size = DEFAULT_PAGE_SIZE;
    }

    public static PageAndSort fromRequest(HttpServletRequest request) {
        PageAndSort pageAndSort = new PageAndSort();
        if (request != null) {

            pageAndSort.setDirection(Direction.of(request.getParameter("sortDir")));

            String property = request.getParameter("sortField");
            pageAndSort.setProperty(isEmpty(property) ? DEFAULT_SORTING_FIELD : property);

            String pageNumber = request.getParameter("page");
            pageAndSort.setPage(isNumeric(pageNumber) ? Integer.parseInt(pageNumber) : DEFAULT_CURRENT_PAGE);

            String size = request.getParameter("size");
            pageAndSort.setSize(isNumeric(size) ? Integer.parseInt(size) : DEFAULT_PAGE_SIZE);
        }
        return pageAndSort;
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public enum Direction {
        ASC,
        DESC;

        public static Direction of(String value) {
            return "DESC".equalsIgnoreCase(value) ? DESC : ASC;
        }
    }

    @Override
    public String toString() {
        return "  page = " + page + "(" + size + ") sort by" + property + " " + direction;
    }
}
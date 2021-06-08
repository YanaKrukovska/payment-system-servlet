package com.krukovska.paymentsystem.controller;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;

public class ServletHelper {
    private static final Logger logger = LogManager.getLogger(ServletHelper.class);

    public static boolean validateIdParam(String attr, String name) throws ServletException {
        //TODO localization
        if (!NumberUtils.isParsable(attr)) {
            logger.error("{} is not valid {}}", attr, name);
            throw new ServletException("Wrong value of accountId");
        } else {
            return true;
        }
    }

}

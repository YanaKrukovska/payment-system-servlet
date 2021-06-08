package com.krukovska.paymentsystem.service;

public class ServiceException extends RuntimeException {
    public ServiceException(String message, Throwable e) {
        super(message, e);
    }

    public ServiceException(String message) {
        super(message);
    }
}

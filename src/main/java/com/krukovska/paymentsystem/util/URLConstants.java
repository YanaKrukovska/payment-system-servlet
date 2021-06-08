package com.krukovska.paymentsystem.util;

public class URLConstants {

    private URLConstants() {}

    public static final String WELCOME = "/welcome";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";

    public static final String ACCOUNT_ALL = "/account-all";
    public static final String ACCOUNT_TOP_UP = "/account-topup";
    public static final String ACCOUNT_BLOCK = "/account-block";

    public static final String CARD = "/card";

    public static final String CLIENT_ALL = "/client-all";
    public static final String CLIENT_BLOCK = "/client-block";
    public static final String CLIENT_UNBLOCK = "/client-unblock";

    public static final String PAYMENT_ALL = "/payment-all";
    public static final String PAYMENT_ADD = "/payment-add";
    public static final String PAYMENT_SEND = "/payment-send";

    public static final String REQUEST_ALL = "/request-all";
    public static final String REQUEST_ACCEPT = "/request-accept";
    public static final String REQUEST_ADD = "/request-add";
    public static final String REQUEST_DECLINE = "/request-decline";

    protected static final String[] LOGIN_REQUIRED_URLS = {
            ACCOUNT_ALL, ACCOUNT_TOP_UP, ACCOUNT_BLOCK, CARD,
            CLIENT_ALL, CLIENT_BLOCK, CLIENT_UNBLOCK, PAYMENT_ALL,
            PAYMENT_ADD, PAYMENT_SEND, REQUEST_ALL, REQUEST_ACCEPT,
            REQUEST_ADD, REQUEST_DECLINE
    };

    protected static final String[] ADMIN_ROLE_REQUIRED_URLS = {
            CLIENT_ALL, CLIENT_BLOCK, CLIENT_UNBLOCK, REQUEST_ACCEPT,
            REQUEST_DECLINE
    };
}

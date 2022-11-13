package com.example.sprintjabackend.constant;

public class SecurityConstant {
    //token expiration time in millisecond = 5 days
    //public static final long TOKEN_EXPIRATION_TIME = 432_000_000;

    //token expiration time in millisecond = 24 hours
    public static final long TOKEN_EXPIRATION_TIME = 86_400_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String SPRINT_JA = "Sprint JA";
    public static final String SPRINT_JA_ADMINISTRATION = "Sprint JA Services";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"**"};

    // TODO: 12/11/2022 add correct public urls..


}

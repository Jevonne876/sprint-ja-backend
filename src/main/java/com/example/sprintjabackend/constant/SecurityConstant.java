package com.example.sprintjabackend.constant;

public class SecurityConstant {

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
    public static final String[] ADMIN_URLS = {"/admin/login", "/admin/sign-up", "/admin/dashboard"};
    public static final String[] PUBLIC_URLS = {"/api/v1/register-new-user", "/api/v1/user-login","/api/v1/reset-password/**","/api/v1/send-query/**"};


}

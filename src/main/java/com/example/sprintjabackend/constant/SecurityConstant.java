package com.example.sprintjabackend.constant;

public class SecurityConstant {

    //token expiration time in millisecond = 24 hours
    public static final long TOKEN_EXPIRATION_TIME = 86_400_000;

    public static final long RESET_PASSWORD_TOKEN_EXPIRATION_TIME = 300000;
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
    public static final String[] PUBLIC_URLS = {"/api/v1/register-new-user",
            "/api/v1/user-login","/api/v1/reset-password/**",
            "/api/v1/send-query/**",
            "/api/v1/admin/admin-login",
            "/api/v1/admin/register-new-admin",
            "/api/v1/admin/register-new-user",
            "/api/v1/admin/get-application-data",
            "/api/v1/admin/get-all-users",
            "/api/v1/admin/get-all-user",
            "/api/v1/admin/view-user/**",
            "/api/v1/admin/update-user",
            "/api/v1/admin/get-all-user-packages",
            "/api/v1/admin/get-all-user-packages-not-shipped",
            "/api/v1/admin/get-all-user-packages-shipped",
            "/api/v1/admin/get-all-user-packages-ready",
            "/api/v1/admin/get-all-user-packages-delivered",
            "/api/v1/admin/register-new-admin",
            "/api/v1/admin/view-user/**",
            "/api/v1/admin/create-new-pre-alert/**",
            "/api/v1/admin/view-package/**",
            "/api/v1/admin/update-package/**",
            "/api/v1/admin/invoice-download/**",
            "/api/v1/admin/get-all-admin-users",
            "/api/v1/admin/delete-user/**",
            "/api/v1/admin/send-email",
            "/api/v1/admin/send-broadcast-email",
            "/api/v1/admin/delete-package/**",
            "/api/v1/admin/file-upload/**",
            "/api/v1/invoice-download/**",
            "/api/v1/admin/export-users",
            "/api/v1/admin/export-packages"
            };


}

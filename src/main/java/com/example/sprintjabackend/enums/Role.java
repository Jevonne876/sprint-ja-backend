package com.example.sprintjabackend.enums;

import static com.example.sprintjabackend.constant.Authorities.*;

public enum Role {

    ROLE_USER(USER_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);
    private final String[] authorities;

    Role(String... userAuthorities) {
        this.authorities = userAuthorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    }

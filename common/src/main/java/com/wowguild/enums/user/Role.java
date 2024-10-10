package com.wowguild.enums.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, RAIDER, OFFICER;

    @Override
    public String getAuthority() {
        return name();
    }


}

package com.wowguild.dto;

import com.wowguild.enums.user.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private long id;
    private String username;
    private boolean active;
    private String email;
    private Set<Role> roles;
    private String language;
}

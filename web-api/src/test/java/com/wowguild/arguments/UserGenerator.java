package com.wowguild.arguments;

import com.wowguild.common.dto.UserDto;
import com.wowguild.common.entity.User;
import com.wowguild.common.enums.user.Role;

import java.util.Collections;

public class UserGenerator {

    public static User generateUser(String name) {
        User user = new User();
        user.setId(1);
        user.setUsername(name);
        user.setPassword("123");
        user.setActive(true);
        user.setEmail("email@email.com");
        user.setActivationCode(null);
        user.setLanguage("EN");
        user.setRoles(Collections.singleton(Role.ADMIN));

        return user;
    }

    public static UserDto generateUserDto(String name) {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername(name);
        userDto.setActive(true);
        userDto.setEmail("email@email.com");
        userDto.setLanguage("EN");
        userDto.setRoles(Collections.singleton(Role.ADMIN));

        return userDto;
    }
}

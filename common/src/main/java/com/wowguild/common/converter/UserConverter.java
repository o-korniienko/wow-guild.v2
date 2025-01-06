package com.wowguild.common.converter;

import com.wowguild.common.dto.security.UserDto;
import com.wowguild.common.entity.security.User;
import org.springframework.stereotype.Service;

@Service
public class UserConverter implements Converter<User, UserDto> {
    @Override
    public User convertToEntity(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto convertToDto(User user) {
        if (user != null) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setRoles(user.getRoles());
            userDto.setLanguage(user.getLanguage());
            userDto.setEmail(user.getEmail());
            userDto.setUsername(user.getUsername());
            userDto.setActive(user.isActive());
            return userDto;
        }
        return null;
    }
}

package com.wowguild.web_api.service.security;

import com.wowguild.common.dto.security.UserDto;
import com.wowguild.common.entity.security.User;
import com.wowguild.common.enums.user.Role;
import com.wowguild.common.service.UserService;
import com.wowguild.common.utils.Encoder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetService implements UserDetailsService {

    private final UserService userService;
    private final Encoder encoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        return user;
    }

    public User findById(long id) {
        return userService.findById(id);
    }

    public String delete(long id) {
        User user = findById(id);
        if (user != null) {
            userService.delete(user);
            return "Deleted";
        }
        throw new EntityNotFoundException();
    }

    public String updateUserLanguage(User user, String language) {
        user.setLanguage(language);
        userService.save(user);
        return "Success";
    }

    public User getUserByUserName(UserDetails user) {
        if (user != null && user.getUsername() != null) {
            return userService.findByUsername(user.getUsername());
        }
        throw new EntityNotFoundException();
    }

    public String registration(UserDto userDto) {
        User user = userService.findByUsername(userDto.getUsername().trim());
        if (user != null) {
            return "Exist";
        }
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user = new User();
        user.setLanguage(userDto.getLanguage());
        user.setActive(true);
        user.setUsername(userDto.getUsername().trim());
        user.setRoles(roles);
        user.setPassword(encoder.encode(userDto.getPass()));
        userService.save(user);
        return "Success";
    }

    public String editUser(User user, boolean isNameChanged) {
        Set<Role> roles = new HashSet<>();
        User userFromDB = userService.findByUsername(user.getUsername().trim());
        if (userFromDB != null && isNameChanged) {
            return "Exist";
        }
        userFromDB = userService.findById(user.getId());
        if (userFromDB != null) {
            userFromDB.setUsername(user.getUsername().trim());
            if (user.getEmail() != null) {
                userFromDB.setEmail(user.getEmail().trim());
            } else {
                userFromDB.setEmail("");
            }
            userFromDB.setActive(user.isActive());
            if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                userFromDB.setPassword(encoder.encode(user.getPassword().trim()));
            }
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                roles.add(Role.USER);
                userFromDB.setRoles(roles);
            } else {
                userFromDB.setRoles(user.getRoles());
            }
            userFromDB.setLanguage(user.getLanguage());
            userService.save(userFromDB);
            return "Saved";
        }
        throw new EntityNotFoundException();
    }

    public Collection<User> findAll() {
        return userService.findAll();
    }
}

package com.wowguild.service;

import com.wowguild.entity.User;
import com.wowguild.enums.user.Role;
import com.wowguild.repos.UserRepos;
import com.wowguild.utils.Encoder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepos userRepos;
    private final Encoder encoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepos.findByUsername(username);
        return user;
    }

    public Collection<User> findAll() {
        return userRepos.findAll();
    }

    public String updateUserLanguage(User user, String language) {
        user.setLanguage(language);
        userRepos.save(user);
        return "Success";
    }

    public User getUserByUserName(UserDetails user) {
        if (user != null && user.getUsername() != null) {
            return userRepos.findByUsername(user.getUsername());
        }
        throw new EntityNotFoundException();
    }

    public String registration(String userName, String password, String language) {
        User user = userRepos.findByUsername(userName.trim());
        if (user != null) {
            return "Exist";
        }
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user = new User();
        user.setLanguage(language);
        user.setActive(true);
        user.setUsername(userName);
        user.setRoles(roles);
        user.setPassword(encoder.encode(password));
        userRepos.save(user);
        return "Success";
    }

    public String deleteUser(long id) {
        User user = userRepos.findById(id).orElse(null);
        if (user != null) {
            userRepos.delete(user);
            return "Deleted";
        }
        throw new EntityNotFoundException();
    }

    public String editUser(User user, boolean isNameChanged) {
        Set<Role> roles = new HashSet<>();
        User userFromDB = userRepos.findByUsername(user.getUsername().trim());
        if (userFromDB != null && isNameChanged) {
            return "Exist";
        }
        Optional<User> userFromDB2 = userRepos.findById(user.getId());
        userFromDB = userFromDB2.get();

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
        userRepos.save(userFromDB);
        return "Saved";
    }

    public User findById(long id) {
        return userRepos.findById(id).orElse(null);
    }
}

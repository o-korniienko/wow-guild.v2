package com.wowguild.service;

import com.wowguild.entity.User;
import com.wowguild.enums.user.Role;
import com.wowguild.repos.UserRepos;
import com.wowguild.utils.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepos userRepos;
    @Autowired
    private Encoder encoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepos.findByUsername(username);
        return user;
    }

    public Collection<User> findAll() {
        return userRepos.findAll();
    }

    public List<String> updateUserLanguage(User user, String language) {
        List<String> result = new ArrayList<>();
        try {
            user.setLanguage(language);
            userRepos.save(user);
        } catch (Exception e) {
            log.error("Could not update user UI language, error {}", e.getMessage());
            return result;
        }
        result.add("Success");
        return result;
    }

    public User getUserByUserName(UserDetails user) {
        if (user != null && user.getUsername() != null) {
            return userRepos.findByUsername(user.getUsername());
        }
        return null;
    }

    public List<String> registration(String userName, String password, String language) {
        List<String> result = new ArrayList<>();
        User user = userRepos.findByUsername(userName.trim());
        if (user != null) {
            result.add("Exist");
            return result;
        }

        try {
            Set<Role> roles = new HashSet<>();
            roles.add(Role.USER);
            user = new User();
            user.setLanguage(language);
            user.setActive(true);
            user.setUsername(userName);
            user.setRoles(roles);
            user.setPassword(encoder.encode(password));
            userRepos.save(user);
            result.add("Success");
            result.add(userName);
            return result;
        } catch (Exception e) {
            log.error("Could not register a new user, error {}", e.getMessage());
            result.add("an internal server error");
            return result;
        }
    }

    public Map<String, Collection<User>> deleteUser(User user) {
        Map<String, Collection<User>> result = new HashMap<>();
        try {
            userRepos.delete(user);
            result.put("Deleted", findAll());
            return result;
        } catch (Exception e) {
            log.error("Could not delete user, error {}", e.getMessage());
            result.put("an internal server error", findAll());
            return result;
        }
    }

    public List<String> editUser(User user, boolean isNameChanged) {
        List<String> result = new ArrayList();
        Set<Role> roles = new HashSet<>();
        User userFromDB = userRepos.findByUsername(user.getUsername().trim());
        if (userFromDB != null && isNameChanged) {
            result.add("Exist");
            return result;
        }
        Optional<User> userFromDB2 = userRepos.findById(user.getId());
        userFromDB = userFromDB2.get();

        try {

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
            if (user.getRoles() == null || user.getRoles().size() == 0) {
                roles.add(Role.USER);
                userFromDB.setRoles(roles);
            } else {
                userFromDB.setRoles(user.getRoles());
            }
            userFromDB.setLanguage(user.getLanguage());
            userRepos.save(userFromDB);
            result.add("Saved");
            return result;
        } catch (Exception e) {
            log.error("Could not update user, error {}", e.getMessage());
            result.add("an internal server error");
            return result;
        }
    }
}

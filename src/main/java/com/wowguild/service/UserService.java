package com.wowguild.service;

import com.wowguild.enums.user.Role;
import com.wowguild.entity.User;
import com.wowguild.repos.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepos userRepos;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepos.findByUsername(s);

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
            e.printStackTrace();
            result.add("an internal server error");
            return result;
        }

        result.add("Success");
        return result;
    }

    public Collection<User> getUserByUserName(User user) {
        List<User> users = new ArrayList<>();
        if (user != null) {
            String username = user.getUsername();
            User userFromDB = userRepos.findByUsername(username);
            users.add(userFromDB);
            return users;
        } else {
            users.add(null);
            return users;
        }
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
            user.setPassword(passwordEncoder.encode(password));
            userRepos.save(user);
            result.add("Success");
            result.add(userName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(LocalDateTime.now() + ": username - " + userName + "; pas - " + password + "; language - " + language);
            System.out.println(LocalDateTime.now() + ": user - " + user);
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
            e.printStackTrace();
            System.out.println(LocalDateTime.now() + ": " + user);
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
                userFromDB.setPassword(passwordEncoder.encode(user.getPassword().trim()));
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
            e.printStackTrace();
            System.out.println(LocalDateTime.now() + ": " + user);
            result.add("an internal server error");
            return result;
        }
    }
}

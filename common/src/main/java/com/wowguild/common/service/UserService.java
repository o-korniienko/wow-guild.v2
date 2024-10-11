package com.wowguild.common.service;

import com.wowguild.common.entity.security.User;
import com.wowguild.common.repos.security.UserRepos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepos userRepos;

    public void save(User user) {
        userRepos.save(user);
    }

    public Collection<User> findAll() {
        return userRepos.findAll();
    }

    public User findById(long id) {
        return userRepos.findById(id).orElse(null);
    }

    public User findByUsername(String name) {
        return userRepos.findByUsername(name);
    }

    public void delete(User user) {
        userRepos.delete(user);
    }
}

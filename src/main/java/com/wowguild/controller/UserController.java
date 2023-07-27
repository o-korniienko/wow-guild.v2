package com.wowguild.controller;

import com.wowguild.entity.User;
import com.wowguild.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/get_user")
    public Collection<User> getUser(@AuthenticationPrincipal User user) {
        return service.getUserByUserName(user);
    }

    @PostMapping("/update_language")
    public List<String> updateUserLanguage(@AuthenticationPrincipal User user, @RequestParam("language") String language){
        return service.updateUserLanguage(user,language);
    }

    @PostMapping("/registration")
    public List<String> registration(@RequestParam("username") String userName, @RequestParam("password") String password, @RequestParam("language") String language){

        return service.registration(userName,password, language);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete_user/{id}")
    public Map<String,Collection<User>> deleteUser(@PathVariable("id") User user) {

        return service.deleteUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get_users")
    public Collection<User> getUsers() {

        return service.findAll();
    }


    @PutMapping("/edit_user")
    public List<String> editUser(@RequestBody User user, @RequestParam("is_name_changed") boolean isNameChanged) {

        return service.editUser(user, isNameChanged);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get_user/{id}")
    public User getUser2(@PathVariable("id") User user) {

        return user;
    }

}

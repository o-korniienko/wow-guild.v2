package com.wowguild.controller;

import com.wowguild.converter.Converter;
import com.wowguild.dto.UserDto;
import com.wowguild.entity.User;
import com.wowguild.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final Converter<User, UserDto> userConverter;

    @GetMapping("/get_user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal User user) {
        UserDto userDto = userConverter.convertToDto(service.getUserByUserName(user));
        return userDto != null ? ResponseEntity.ok(userDto) : ResponseEntity.notFound().build();
    }

    @PostMapping("/update_language")
    public List<String> updateUserLanguage(@AuthenticationPrincipal User user, @RequestParam("language") String language) {
        return service.updateUserLanguage(user, language);
    }

    @PostMapping("/registration")
    public List<String> registration(@RequestParam("username") String userName, @RequestParam("password") String password, @RequestParam("language") String language) {
        return service.registration(userName, password, language);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete_user/{id}")
    public Map<String, Collection<User>> deleteUser(@PathVariable("id") User user) {
        return service.deleteUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get_users")
    public Collection<UserDto> getUsers() {
        return service.findAll().stream()
                .map(userConverter::convertToDto)
                .collect(Collectors.toList());
    }


    @PutMapping("/edit_user")
    public List<String> editUser(@RequestBody User user, @RequestParam("is_name_changed") boolean isNameChanged) {
        return service.editUser(user, isNameChanged);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get_user/{id}")
    public UserDto getUser2(@PathVariable("id") User user) {
        return userConverter.convertToDto(user);
    }

}

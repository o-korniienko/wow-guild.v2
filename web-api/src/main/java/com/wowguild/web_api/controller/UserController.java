package com.wowguild.web_api.controller;

import com.wowguild.common.converter.Converter;
import com.wowguild.common.dto.security.UserDto;
import com.wowguild.common.dto.api.ApiResponse;
import com.wowguild.common.entity.security.User;
import com.wowguild.web_api.service.security.UserDetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserDetService service;
    private final Converter<User, UserDto> userConverter;

    @GetMapping("/get-active")
    public ResponseEntity<?> getActiveUser(@AuthenticationPrincipal User user) {
        try {
            UserDto userDto = userConverter.convertToDto(service.getUserByUserName(user));
            return userDto != null ? ResponseEntity.ok(userDto) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Could not get user data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get user data");
        }
    }

    @PostMapping("/update-language")
    public ResponseEntity<?> updateUserLanguage(@AuthenticationPrincipal User user, @RequestParam("language") String language) {
        String resultStatus = service.updateUserLanguage(user, language);
        return ResponseEntity.ok(new ApiResponse<>(resultStatus, 200, language));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserDto userDto) {
        try {
            String resultStatus = service.registration(userDto);
            return ResponseEntity.ok(new ApiResponse<>(resultStatus, 200, userDto.getUsername()));
        } catch (Exception e) {
            log.error("Could not create new user. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not create new user");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete-one/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        try {
            String resultStatus = service.delete(id);
            return ResponseEntity.ok(new ApiResponse<>(resultStatus, 200, service.findAll().stream()
                    .map(userConverter::convertToDto)
                    .collect(Collectors.toList())));
        } catch (Exception e) {
            log.error("Could not remove user {}. Error: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body("Could not remove user.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getUsers() {
        try {
            return ResponseEntity.ok(service.findAll().stream()
                    .map(userConverter::convertToDto)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Could not get users data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get users data.");
        }
    }


    @PutMapping("/edit")
    public ResponseEntity<?> editUser(@RequestBody User user, @RequestParam("is_name_changed") boolean isNameChanged) {
        try {
            String resultStatus = service.editUser(user, isNameChanged);
            return ResponseEntity.ok(new ApiResponse<>(resultStatus, 200));
        } catch (Exception e) {
            log.error("Could not update user. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not update user.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get-one/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") long id) {
        try {
            UserDto userDto = userConverter.convertToDto(service.findById(id));
            if (userDto != null) {
                return ResponseEntity.ok(userDto);
            } else {
                log.error("Could not find user {}.", id);
                return ResponseEntity.badRequest().body("Could not find user.");
            }
        } catch (Exception e) {
            log.error("Could not get user {} data. Error: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body("Could not get user data.");
        }
    }

}

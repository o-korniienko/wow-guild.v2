package com.wowguild.web_api.controller;

import com.wowguild.common.dto.api.ApiResponse;
import com.wowguild.common.entity.InformingMessage;
import com.wowguild.web_api.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService service;

    @GetMapping("get_greeting_message")
    public ResponseEntity<?> getGreetingMessage() {
        return ResponseEntity.ok(service.getGreetingMessage());
    }

    @PostMapping("save_greeting")
    public ResponseEntity<?> saveGreeting(@RequestBody InformingMessage message) {
        try {
            String resultStatus = service.saveGreeting(message);
            return ResponseEntity.ok(new ApiResponse<>(resultStatus, 200));
        } catch (Exception e) {
            log.error("Could not save greeting message. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not save greeting message.");
        }
    }

    @PostMapping("update_message_by_tag")
    public ResponseEntity<?> updateMessageByTag(@RequestBody InformingMessage message) {
        try {
            String resultStatus = service.updateMessageByTag(message);
            return ResponseEntity.ok(new ApiResponse<>(resultStatus, 200));
        } catch (Exception e) {
            log.error("Could not update message by tag. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not update message by tag.");
        }
    }

    @GetMapping("get_about_us_messages")
    public ResponseEntity<?> getAboutUSMessages() {
        try {
            return ResponseEntity.ok(service.getAboutUsMessages());
        } catch (Exception e) {
            log.error("Could not get about guild messages. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get about guild messages");
        }
    }
}

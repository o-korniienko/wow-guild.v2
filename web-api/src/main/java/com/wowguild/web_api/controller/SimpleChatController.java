package com.wowguild.web_api.controller;

import com.wowguild.common.dto.SimpleChatMessageDto;
import com.wowguild.common.dto.api.ApiResponse;
import com.wowguild.common.entity.SimpleChatMessage;
import com.wowguild.web_api.service.SimpleChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/simple-chat")
public class SimpleChatController {

    private final SimpleChatService simpleChatService;

    @GetMapping("/message/get-all")
    public ResponseEntity<?> getAllSimpleChatMessages() {
        return ResponseEntity.ok(simpleChatService.getAllSimpleChatMessages());
    }

    @PostMapping("/message/send")
    public ResponseEntity<?> saveSimpleChatMessage(@RequestBody SimpleChatMessageDto messageDto) {
        try {
           SimpleChatMessage simpleChatMessage = simpleChatService.processNewMessage(messageDto);
            return ResponseEntity.ok(new ApiResponse<>("saved", 200, simpleChatMessage));
        } catch (IllegalArgumentException e) {
            log.error("Could not process simple chat message {}. Error: {}", messageDto, e.getMessage());

            return ResponseEntity.badRequest().body("Could not process simple chat message. message is null.");
        } catch (Exception e) {
            log.error("Could not process simple chat message {}. Error: {}", messageDto, e.getMessage());

            return ResponseEntity.internalServerError().body("Could not process simple chat message.");
        }
    }
}

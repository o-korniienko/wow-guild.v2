package com.wowguild.controller;

import com.wowguild.entity.InformingMessage;
import com.wowguild.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService service;

    @GetMapping("get_greeting_message")
    public List<InformingMessage> getGreetingMessage() {
        return service.getGreetingMessage();
    }

    @PostMapping("save_greeting")
    public List<String> saveGreeting(@RequestBody InformingMessage message) {
        return service.saveGreeting(message);
    }

    @PostMapping("update_message_by_tag")
    public List<String> updateMessageByTag(@RequestBody InformingMessage message) {
        return service.updateMessageByTag(message);
    }

    @GetMapping("get_about_us_messages")
    public List<InformingMessage> getAboutUSMessages() {
        return service.getAboutUsMessages();
    }
}

package com.after_dark.controller;

import com.after_dark.model.InformingMessage;
import com.after_dark.repos.InfMessageRepos;
import com.after_dark.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private MessageService service;

    @GetMapping("get_greeting_message")
    public List<InformingMessage>  getGreetingMessage(){
        return service.getGreetingMessage();
    }

    @PostMapping("save_greeting")
    public List<String> saveGreeting(@RequestBody InformingMessage message){
       return service.saveGreeting(message);
    }

    @PostMapping("update_message_by_tag")
    public List<String> updateMessageByTag(@RequestBody InformingMessage message){

        return service.updateMessageByTag(message);
    }

    @GetMapping("get_about_us_messages")
    public List<InformingMessage> getAboutUSMessages(){
        return service.getAboutUsMessages();
    }
}

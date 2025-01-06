package com.wowguild.web_api.service;

import com.wowguild.common.entity.InformingMessage;
import com.wowguild.common.repos.InfMessageRepos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final InfMessageRepos infMessageRepos;


    public List<InformingMessage> getGreetingMessage() {
        return infMessageRepos.findByTag("greeting");
    }


    public String saveGreeting(InformingMessage message) {
        List<InformingMessage> messages = infMessageRepos.findByTag("greeting");

        if (messages == null || messages.isEmpty()) {
            InformingMessage newMessage = new InformingMessage();
            newMessage.setTag("greeting");
            newMessage.setMessage(message.getMessage());
            infMessageRepos.save(message);
            return "Successful";
        } else {
            InformingMessage messageFromDB = messages.get(0);
            messageFromDB.setMessage(message.getMessage());
            infMessageRepos.save(messageFromDB);
            return "Successful";
        }
    }

    public String updateMessageByTag(InformingMessage message) {
        List<InformingMessage> messages = infMessageRepos.findByTag(message.getTag());
        if (messages == null || messages.isEmpty()) {
            InformingMessage newMessage = new InformingMessage();
            newMessage.setTag(message.getTag());
            newMessage.setMessage(message.getMessage());
            infMessageRepos.save(message);
            return "Successful";
        } else {
            InformingMessage messageFromDB = messages.get(0);
            messageFromDB.setMessage(message.getMessage());
            infMessageRepos.save(messageFromDB);
            return "Successful";
        }
    }

    public List<InformingMessage> getAboutUsMessages() {
        List<InformingMessage> allMessages = new ArrayList<>();
        List<InformingMessage> messages = new ArrayList<>();
        messages = infMessageRepos.findByTag("general");
        if (messages != null && messages.size() > 0) {
            allMessages.add(messages.get(0));
        }
        messages = infMessageRepos.findByTag("about");
        if (messages != null && messages.size() > 0) {
            allMessages.add(messages.get(0));
        }
        messages = infMessageRepos.findByTag("contacts");
        if (messages != null && messages.size() > 0) {
            allMessages.add(messages.get(0));
        }

        return allMessages;
    }
}

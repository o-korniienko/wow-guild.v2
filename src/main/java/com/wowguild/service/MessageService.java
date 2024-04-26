package com.wowguild.service;

import com.wowguild.entity.InformingMessage;
import com.wowguild.repos.InfMessageRepos;
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
        List<InformingMessage> greetingMessages = new ArrayList<>();
        greetingMessages = infMessageRepos.findByTag("greeting");
        if (greetingMessages != null && greetingMessages.size() > 0) {
            return greetingMessages;
        }
        return greetingMessages;
    }


    public List<String> saveGreeting(InformingMessage message) {
        List<String> result = new ArrayList<>();

        try {
            List<InformingMessage> messages = infMessageRepos.findByTag("greeting");

            if (messages == null || messages.size() == 0) {
                InformingMessage newMessage = new InformingMessage();
                newMessage.setTag("greeting");
                newMessage.setMessage(message.getMessage());
                infMessageRepos.save(message);
                result.add("Successful");
                return result;
            } else {
                InformingMessage messageFromDB = messages.get(0);
                messageFromDB.setMessage(message.getMessage());
                infMessageRepos.save(messageFromDB);
                result.add("Successful");
                return result;
            }
        } catch (Exception e) {
            log.error("Could not save greeting message {}, error: {}", message, e.getMessage());
            result.add("an internal server error");
            return result;
        }
    }

    public List<String> updateMessageByTag(InformingMessage message) {
        List<String> result = new ArrayList<>();
        try {
            List<InformingMessage> messages = infMessageRepos.findByTag(message.getTag());

            if (messages == null || messages.size() == 0) {
                InformingMessage newMessage = new InformingMessage();
                newMessage.setTag(message.getTag());
                newMessage.setMessage(message.getMessage());
                infMessageRepos.save(message);
                result.add("Successful");
                return result;
            } else {
                InformingMessage messageFromDB = messages.get(0);
                messageFromDB.setMessage(message.getMessage());
                infMessageRepos.save(messageFromDB);
                result.add("Successful");
                return result;
            }
        } catch (Exception e) {
            log.error("Could not update message {}, error: {}", message, e.getMessage());
            result.add("an internal server error");
            return result;
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

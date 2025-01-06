package com.wowguild.push_api.sender;

import org.springframework.stereotype.Service;

@Service
public interface FirebaseMessageSender {

    void send(String title, String body, String token);
}

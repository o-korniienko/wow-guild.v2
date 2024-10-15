package com.wowguild.push_api.sender;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class IOSMessageSender implements FirebaseMessageSender{

    @Override
    public void send(String title, String body, String token) {
        try {
            Message message = Message.builder()
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setAlert(ApsAlert.builder()
                                            .setTitle(title)
                                            .setBody(body)
                                            .build())
                                    .build())
                            .build())
                    .setToken(token)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);

            log.info("Sent successfully: {}", response);
        } catch (Exception e) {
            log.error("Could not send firebase message. Error: {}", e.getMessage());
        }
    }
}

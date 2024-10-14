package com.wowguild.push_api.sender;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AndroidMessageSender implements FirebaseMessageSender {

    @Override
    public void send(String title, String body, String firebaseDeviceToken) {
        try {
            AndroidNotification notification = AndroidNotification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();
            Message message = Message.builder()
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(notification)
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .setToken(firebaseDeviceToken)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);

            log.info("Sent successfully: {}", response);
        } catch (Exception e) {
            log.error("Could not send firebase message. Error: {}", e.getMessage());
        }
    }
}

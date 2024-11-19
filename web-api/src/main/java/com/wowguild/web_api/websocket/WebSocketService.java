package com.wowguild.web_api.websocket;

import com.wowguild.web_api.websocket.model.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    private static final String TOPIC_URI = "/topic/simple_chat_message";

    private final SimpMessagingTemplate simpMessagingTemplate;


    public void process(WebSocketMessage<?> message) {
        try {
            simpMessagingTemplate.convertAndSend(TOPIC_URI, message);
            log.info("Message was sent to WebSocket: {}", message);
        } catch (Exception e) {
            throw new RuntimeException("Error send websocket " + e.getMessage());
        }
    }
}

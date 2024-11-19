package com.wowguild.web_api.service;

import com.wowguild.common.converter.SimpleChatMessageConverter;
import com.wowguild.common.dto.SimpleChatMessageDto;
import com.wowguild.common.entity.SimpleChatMessage;
import com.wowguild.common.service.impl.SimpleChatMessageService;
import com.wowguild.web_api.websocket.WebSocketService;
import com.wowguild.web_api.websocket.model.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleChatService {

    private final WebSocketService webSocketService;
    private final SimpleChatMessageService simpleChatMessageService;
    private final SimpleChatMessageConverter simpleChatMessageConverter;
    private final ExecutorService executorService;

    public static Comparator<SimpleChatMessage> BY_DATE = (o1, o2) ->
            (int) (o1.getDate().toEpochSecond(ZoneOffset.UTC) - o2.getDate().toEpochSecond(ZoneOffset.UTC));


    public List<SimpleChatMessage> getAllSimpleChatMessages() {
        return simpleChatMessageService.getAllSortedBy(BY_DATE);
    }


    public SimpleChatMessage processNewMessage(SimpleChatMessageDto messageDto) {
        if (messageDto != null) {
            messageDto.setDate(LocalDateTime.now());
            SimpleChatMessage message = simpleChatMessageConverter.convertToEntity(messageDto);
            simpleChatMessageService.save(message);

            executorService.execute(() -> {
                WebSocketMessage<SimpleChatMessageDto> webSocketMessage = new WebSocketMessage<>();
                webSocketMessage.setId(String.valueOf(message.getId()));
                webSocketMessage.setTime(LocalDateTime.now());
                webSocketMessage.setData(messageDto);

                webSocketService.process(webSocketMessage);
            });
            return message;
        } else {
            throw new IllegalArgumentException("Message is null");
        }
    }
}

package com.wowguild.web_api.websocket.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WebSocketMessage<DataObject> {

    private String id;
    private LocalDateTime time;
    private String messageType = "object";
    private DataObject data;
}

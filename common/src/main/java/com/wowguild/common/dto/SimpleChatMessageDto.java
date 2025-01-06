package com.wowguild.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleChatMessageDto {

    private long id;
    private String message;
    private LocalDateTime date;
    private long userId;
}

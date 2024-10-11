package com.wowguild.arguments;

import com.wowguild.common.dto.GuildEventDto;
import com.wowguild.common.entity.GuildEvent;

import java.time.LocalDateTime;

public class GuildEventGenerator {

    public static GuildEvent generateGuildEvent(String subject, long id, String message, LocalDateTime date){
        GuildEvent event = new GuildEvent();
        event.setMessage(message);
        event.setSubject(subject);
        event.setId(id);
        event.setDate(date);

        return event;
    }

    public static GuildEventDto generateGuildEventDto(String subject, long id, String message, LocalDateTime date){
        GuildEventDto event = new GuildEventDto();
        event.setMessage(message);
        event.setSubject(subject);
        event.setId(id);
        event.setDate(date);

        return event;
    }
}

package com.wowguild.arguments;

import com.wowguild.common.entity.GuildEvent;

import java.time.LocalDateTime;
import java.util.Set;

public class GuildEventGenerator {

    public static GuildEvent getGuildEvent() {
        GuildEvent event = new GuildEvent();
        event.setId(1);
        event.setDate(LocalDateTime.now().plusSeconds(10));
        event.setMessage("Guild event is coming");
        event.setSubject("Guild Event");

        Set<Long> userIDs = Set.of(21L, 31L, 32L);
        event.setUserIDs(userIDs);

        return event;
    }
}

package com.wowguild.arguments;

import com.wowguild.common.model.kafka.GuildEventMessage;

import java.time.LocalDateTime;

public class KafkaMessageGenerator {

    public static GuildEventMessage getGuildEventMessage(){
        GuildEventMessage message = new GuildEventMessage();
        message.setMessage("Guild event is coming");
        message.setEventId(123);
        message.setSubject("Guild Event");
        message.setScheduleDate(LocalDateTime.now().plusSeconds(10));

        return message;
    }
}

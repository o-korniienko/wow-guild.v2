package com.wowguild.common.model.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class GuildEventMessage extends KafkaMessage {

    private String subject;
    private LocalDateTime scheduleDate;
    private long eventId;
}

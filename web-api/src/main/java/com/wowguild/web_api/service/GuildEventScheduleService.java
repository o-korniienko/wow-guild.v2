package com.wowguild.web_api.service;

import com.wowguild.common.converter.GuildEventConverter;
import com.wowguild.common.dto.GuildEventDto;
import com.wowguild.common.dto.wow.UpdateStatus;
import com.wowguild.common.entity.GuildEvent;
import com.wowguild.common.model.kafka.KafkaMessage;
import com.wowguild.common.model.kafka.GuildEventMessage;
import com.wowguild.common.service.impl.GuildEventService;
import com.wowguild.web_api.sender.KafkaSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuildEventScheduleService {

    private final KafkaSender kafkaSender;
    private final GuildEventConverter guildEventConverter;
    private final GuildEventService guildEventService;

    public UpdateStatus<?> createEventSchedule(GuildEventDto guildEventDto) {
        GuildEvent event = guildEventConverter.convertToEntity(guildEventDto);
        if (event != null) {
            guildEventService.save(event);

            KafkaMessage kafkaMessage = buildKafkaMessage(event);
            if (kafkaSender.sendMessage(kafkaMessage)) {
                return new UpdateStatus<>("Successful", guildEventDto, 200);
            }
            return new UpdateStatus<>("Event has been created, " +
                    "but could not create event schedule", guildEventDto, 500);
        }
        return new UpdateStatus<>("Could not create event", guildEventDto, 500);
    }

    private KafkaMessage buildKafkaMessage(GuildEvent event) {
        GuildEventMessage message = new GuildEventMessage();
        message.setSubject(event.getSubject());
        message.setMessage(event.getMessage());
        message.setEventId(event.getId());
        message.setScheduleDate(event.getDate());

        return message;
    }
}

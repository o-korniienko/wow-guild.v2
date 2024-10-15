package com.wowguild.web_api.service;

import com.wowguild.common.converter.GuildEventConverter;
import com.wowguild.common.dto.GuildEventDto;
import com.wowguild.common.dto.wow.UpdateStatus;
import com.wowguild.common.entity.GuildEvent;
import com.wowguild.common.entity.security.User;
import com.wowguild.common.model.kafka.GuildEventMessage;
import com.wowguild.common.model.kafka.KafkaMessage;
import com.wowguild.common.service.impl.GuildEventService;
import com.wowguild.web_api.sender.KafkaSender;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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
                log.info("Event {} has been created.", event.getId());
                return new UpdateStatus<>("Successful", guildEventDto, 200);
            }
            log.info("Event {} has been created. Could not create event schedule.", event.getId());
            return new UpdateStatus<>("Event has been created, " +
                    "but could not create event schedule", guildEventDto, 500);
        }
        log.info("Could not create event {}.", guildEventDto);
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

    public void subscribe(User user, long id) {
        GuildEvent event = guildEventService.findById(id);
        if (event != null) {
            if (user != null) {
                Set<Long> userIDs = event.getUserIDs();
                if (userIDs != null) {
                    userIDs.add(user.getId());
                } else {
                    userIDs = new HashSet<>();
                    userIDs.add(user.getId());
                }
                event.setUserIDs(userIDs);
                guildEventService.save(event);
            } else {
                throw new EntityNotFoundException("User is not authorized");
            }
        } else {
            throw new EntityNotFoundException("Event " + id + " does not exist");
        }
    }
}

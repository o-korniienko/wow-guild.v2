package com.wowguild.push_api.scheduler;

import com.wowguild.common.model.kafka.GuildEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuildEventNotificationScheduler implements TaskTScheduler<GuildEventMessage> {

    private final TaskScheduler scheduler;

    @Override
    public void schedule(GuildEventMessage message) {
        try {
            scheduler.schedule(() -> {
                //to remove
                log.info("Event notification: subject {}, text {}, time {}, event id {}",
                        message.getSubject(), message.getMessage(), message.getScheduleDate(), message.getEventId());

                //TODO send push notification to subscribers
            }, Instant.ofEpochSecond(message.getScheduleDate()
                    .toEpochSecond(ZonedDateTime.now(ZoneId.systemDefault()).getOffset())));
        } catch (Exception e) {
            log.error("Could not schedule event notification task. Error: {}", e.getMessage());
        }
    }
}

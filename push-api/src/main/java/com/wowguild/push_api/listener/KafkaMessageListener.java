package com.wowguild.push_api.listener;

import com.wowguild.common.model.kafka.KafkaMessage;
import com.wowguild.common.model.kafka.GuildEventMessage;
import com.wowguild.push_api.scheduler.GuildEventNotificationScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.wowguild.common.constants.KafkaConstants.EVENT_SCHEDULE_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageListener {

    private final GuildEventNotificationScheduler guildEventNotificationScheduler;

    @KafkaListener(topics = EVENT_SCHEDULE_TOPIC, groupId = "notification_consumer_1")
    public void listen(KafkaMessage message) {
        log.info("Received kafka message {}", message);
        guildEventNotificationScheduler.schedule((GuildEventMessage) message);
    }
}

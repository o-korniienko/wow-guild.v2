package com.wowguild.scheduler;

import com.wowguild.common.model.kafka.GuildEventMessage;
import com.wowguild.push_api.PushApi;
import com.wowguild.push_api.scheduler.GuildEventNotificationScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.wowguild.arguments.KafkaMessageGenerator.getGuildEventMessage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = PushApi.class)
@ActiveProfiles("test")
public class GuildEventNotificationSchedulerTest {

    @Autowired
    private GuildEventNotificationScheduler guildEventNotificationScheduler;

    private GuildEventMessage guildEventMessage;

    @BeforeEach
    void setUp() {
        guildEventMessage = getGuildEventMessage();
    }

    @Test
    void scheduleTest(){
        assertDoesNotThrow(() -> guildEventNotificationScheduler.schedule(guildEventMessage));
    }
}

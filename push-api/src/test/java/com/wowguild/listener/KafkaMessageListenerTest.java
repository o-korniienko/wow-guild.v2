package com.wowguild.listener;

import com.wowguild.common.model.kafka.GuildEventMessage;
import com.wowguild.push_api.PushApi;
import com.wowguild.push_api.listener.KafkaMessageListener;
import com.wowguild.push_api.scheduler.GuildEventNotificationScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static com.wowguild.arguments.KafkaMessageGenerator.getGuildEventMessage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = PushApi.class)
@ActiveProfiles("test")
public class KafkaMessageListenerTest {

    @MockBean
    private GuildEventNotificationScheduler guildEventNotificationScheduler;
    @Autowired
    private KafkaMessageListener kafkaMessageListener;

    private GuildEventMessage guildEventMessage;


    @BeforeEach
    void setUp() {
        guildEventMessage = getGuildEventMessage();
    }

    @Test
    void listenTest() {
        assertDoesNotThrow(() -> kafkaMessageListener.listen(guildEventMessage));
    }
}

package com.wowguild.service;

import com.wowguild.common.entity.GuildEvent;
import com.wowguild.common.entity.push.PushDevice;
import com.wowguild.common.enums.push.OsType;
import com.wowguild.common.model.kafka.GuildEventMessage;
import com.wowguild.common.service.impl.GuildEventService;
import com.wowguild.common.service.impl.PushDeviceService;
import com.wowguild.push_api.sender.AndroidMessageSender;
import com.wowguild.push_api.sender.IOSMessageSender;
import com.wowguild.push_api.service.firebase.NotificationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.wowguild.arguments.GuildEventGenerator.getGuildEvent;
import static com.wowguild.arguments.KafkaMessageGenerator.getGuildEventMessage;
import static com.wowguild.arguments.PushDeviceGenerator.getPushDevice;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationManagerTest {

    @Mock
    private AndroidMessageSender androidMessageSender;
    @Mock
    private IOSMessageSender iosMessageSender;
    @Mock
    private GuildEventService guildEventService;
    @Mock
    private PushDeviceService pushDeviceService;
    @InjectMocks
    private NotificationManager notificationManager;

    private GuildEventMessage guildEventMessage;


    @BeforeEach
    void setUp() {
        guildEventMessage = getGuildEventMessage();
    }

    @ParameterizedTest
    @MethodSource("processEventNotificationArgs")
    void processEventNotificationTest(PushDevice device, GuildEvent event) {
        when(guildEventService.findById(anyLong())).thenReturn(event);
        when(pushDeviceService.findByUserId(anyLong())).thenReturn(device);

        assertDoesNotThrow(() -> notificationManager.processEventNotification(guildEventMessage));

        int amount = event.getUserIDs().size();

        if (device.getOsType().equals(OsType.Android)) {
            verify(androidMessageSender, times(amount)).send(any(), any(), any());
            verify(iosMessageSender, never()).send(any(), any(), any());
        } else {
            verify(iosMessageSender, times(amount)).send(any(), any(), any());
            verify(androidMessageSender, never()).send(any(), any(), any());
        }
    }

    static Stream<Arguments> processEventNotificationArgs() {
        PushDevice device1 = getPushDevice(1L, OsType.Android);
        PushDevice device2 = getPushDevice(2L, OsType.iOS);
        GuildEvent event = getGuildEvent();

        return Stream.of(
                Arguments.of(device1, event),
                Arguments.of(device2, event)
        );
    }

}

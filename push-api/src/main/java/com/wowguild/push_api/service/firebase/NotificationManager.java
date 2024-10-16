package com.wowguild.push_api.service.firebase;

import com.wowguild.common.entity.GuildEvent;
import com.wowguild.common.entity.push.PushDevice;
import com.wowguild.common.model.kafka.GuildEventMessage;
import com.wowguild.common.service.impl.GuildEventService;
import com.wowguild.common.service.impl.PushDeviceService;
import com.wowguild.push_api.sender.AndroidMessageSender;
import com.wowguild.push_api.sender.IOSMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationManager {

    private final AndroidMessageSender androidMessageSender;
    private final IOSMessageSender iosMessageSender;
    private final GuildEventService guildEventService;
    private final PushDeviceService pushDeviceService;

    public void processEventNotification(GuildEventMessage message) {
        GuildEvent event = guildEventService.findById(message.getEventId());
        if (event != null && event.getUserIDs() != null) {
            event.getUserIDs().forEach(userId -> {
                PushDevice device = pushDeviceService.findByUserId(userId);
                if (device != null) {
                    switch (device.getOsType()) {
                        case Android:
                            androidMessageSender.send(message.getSubject() + ". " + message.getScheduleDate(),
                                    message.getMessage(), device.getPushToken());
                            break;
                        case iOS:
                            iosMessageSender.send(message.getSubject() + ". " + message.getScheduleDate(),
                                    message.getMessage(), device.getPushToken());
                            break;
                    }
                }
            });
        }
    }
}

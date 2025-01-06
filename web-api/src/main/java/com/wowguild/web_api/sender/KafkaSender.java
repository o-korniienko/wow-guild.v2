package com.wowguild.web_api.sender;

import com.wowguild.common.model.kafka.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.wowguild.common.constants.KafkaConstants.EVENT_SCHEDULE_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaSender {

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public boolean sendMessage(KafkaMessage message) {
        try {
            /*Message<KafkaMessage> mss = MessageBuilder
                    .withPayload(message)
                    .setHeader(KafkaHeaders.TOPIC, EVENT_SCHEDULE_TOPIC)
                    .build();
            kafkaTemplate.send(message);*/
            kafkaTemplate.send(EVENT_SCHEDULE_TOPIC, message);
            return true;
        } catch (Exception e) {
            log.error("Could not send message to Kafka. Error {}", e.getMessage());
            return false;
        }
    }
}

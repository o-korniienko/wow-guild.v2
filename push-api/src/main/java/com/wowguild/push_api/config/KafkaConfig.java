package com.wowguild.push_api.config;


import com.wowguild.common.model.kafka.KafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${kafka.servers}")
    private String kafkaServers;

    @Bean
    public ConsumerFactory<String, KafkaMessage> consumerFactory() {
        JsonDeserializer<KafkaMessage> deserializer = new JsonDeserializer<>(KafkaMessage.class);
        //deserializer.setRemoveTypeHeaders(false);
        //deserializer.addTrustedPackages("wowguild.common.model.kafka");
        //deserializer.setUseTypeMapperForKey(true);


        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        //props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, KafkaMessage.class.getName());
        //props.put(JsonDeserializer.TRUSTED_PACKAGES, "wowguild.common.model.kafka");
        //props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        //return new DefaultKafkaConsumerFactory<>(props);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

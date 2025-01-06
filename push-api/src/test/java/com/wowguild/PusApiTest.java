package com.wowguild;

import com.wowguild.push_api.PushApi;
import com.wowguild.push_api.config.KafkaConfig;
import com.wowguild.push_api.config.PushApiConf;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {PushApi.class})
@ActiveProfiles("test")
public class PusApiTest {

    @Autowired
    private PushApiConf pushApiConf;
    @Autowired
    private KafkaConfig kafkaConfig;

    @Test
    void testPasswordEncoderBean(){
        assertNotNull(pushApiConf.getPasswordEncoder());
    }

    @Test
    void testTaskSchedulerBean(){
        assertNotNull(pushApiConf.taskScheduler());
    }

    @Test
    void testConcurrentKafkaListenerContainerFactoryBean(){
        assertNotNull(kafkaConfig.kafkaListenerContainerFactory());
    }
}

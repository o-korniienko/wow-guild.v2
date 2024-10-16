package com.wowguild.sender;

import com.wowguild.push_api.PushApi;
import com.wowguild.push_api.sender.IOSMessageSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = PushApi.class)
@ActiveProfiles("test")
public class IOSMessageSenderTest {

    @Autowired
    private IOSMessageSender iosMessageSender;

    @Test
    void sendTest(){
        assertDoesNotThrow(() -> iosMessageSender.send("title", "message", "firebase token"));
    }
}

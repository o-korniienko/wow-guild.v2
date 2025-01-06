package com.wowguild.sender;

import com.wowguild.push_api.PushApi;
import com.wowguild.push_api.sender.AndroidMessageSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = PushApi.class)
@ActiveProfiles("test")
public class AndroidMessageSenderTest {

    @Autowired
    private AndroidMessageSender androidMessageSender;

    @Test
    void sendTest(){
        assertDoesNotThrow(() -> androidMessageSender.send("title", "message", "firebase token"));
    }
}

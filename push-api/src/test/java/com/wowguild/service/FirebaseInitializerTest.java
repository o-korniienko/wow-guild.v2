package com.wowguild.service;

import com.wowguild.push_api.PushApi;
import com.wowguild.push_api.service.firebase.FirebaseInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = PushApi.class)
@ActiveProfiles("test")
public class FirebaseInitializerTest {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @Test
    void initializeTest() {
       assertDoesNotThrow(() -> firebaseInitializer.initialize());
    }
}

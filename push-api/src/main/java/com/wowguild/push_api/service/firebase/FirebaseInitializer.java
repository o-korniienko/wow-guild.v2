package com.wowguild.push_api.service.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Service
public class FirebaseInitializer {

    @Value("${firebase.app.keys.file}")
    private String appKeysFile;

    @PostConstruct
    public void initialize() throws IOException {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(appKeysFile);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            log.info("Firebase app has been initialized");
        } catch (IOException e) {
            log.error("Could not initialize Firebase app. Error: {}", e.getMessage());
        }
    }
}

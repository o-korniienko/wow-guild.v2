package com.wowguild.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
@Slf4j
public class LogHandler {

    public void saveLog(String logMessage, String fileName) {
        logMessage = "\n" + logMessage;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(logMessage);

        } catch (IOException e) {
            log.error("Error writing log file", e);
        }
    }
}

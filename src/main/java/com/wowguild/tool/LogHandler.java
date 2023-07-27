package com.wowguild.tool;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class LogHandler {

    public void saveLog(String log, String fileName) {
        log = "\n" + log;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(log);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

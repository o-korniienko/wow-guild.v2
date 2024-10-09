package com.wowguild.tool;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogHandlerTest {

    private static LogHandler logService;
    private static final String TEST_FILE_NAME = "src/test/resources/test-log.txt";

    @BeforeAll
    static void setup() {
        logService = new LogHandler();
    }

    @Test
    void testSaveLog_createsLogFile() {
        String logMessage1 = "This is the first test log message.";
        String logMessage2 = "This is the second test log message.";

        logService.saveLog(logMessage1, TEST_FILE_NAME);
        logService.saveLog(logMessage2, TEST_FILE_NAME);

        File logFile = new File(TEST_FILE_NAME);
        assertTrue(logFile.length() > 0, "Log file should not be empty after writing.");

        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            String line;
            boolean containsLogMessage1 = false;
            boolean containsLogMessage2 = false;

            while ((line = br.readLine()) != null) {
                if (line.trim().equals(logMessage1)) {
                    containsLogMessage1 = true;
                }
                if (line.trim().equals(logMessage2)) {
                    containsLogMessage2 = true;
                }
            }
            assertTrue(containsLogMessage1, "Log file should contain the first log message.");
            assertTrue(containsLogMessage2, "Log file should contain the second log message.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void cleanup() {
        File logFile = new File(TEST_FILE_NAME);
        if (logFile.exists()) {
            logFile.delete();
        }
    }
}

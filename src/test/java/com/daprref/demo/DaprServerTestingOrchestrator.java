package com.daprref.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DaprServerTestingOrchestrator {
    private Process daprProcess = null;
    private Process daprStopProcess = null;
    private Process applicationStopProcess = null;

    private static String DAPR_START_COMMAND = "dapr run --app-id addcalculator --app-port 8080 --port 3500 -- mvn spring-boot:start";
    private String DAPR_START_PROCESS_TEXT_COMPLETED_TRIGGER = "dapr initialized. Status: Running";

    private String DAPR_STOP_COMMAND = "dapr stop --app-id addcalculator";
    private String DAPR_STOP_PROCESS_TEXT_COMPLETED_TRIGGER = "app stopped successfully";


    private String APPLICATION_STOP_COMMAND = "mvn spring-boot:stop";
    private String APPLICATION_STOP_PROCESS_TEXT_COMPLETED_TRIGGER = "Finished at";

    @BeforeAll
    private void beforeAll() {
        daprProcess = runCommandAndWaitUntil(DAPR_START_COMMAND, DAPR_START_PROCESS_TEXT_COMPLETED_TRIGGER);
    }

    @AfterAll
    private void afterAll() throws IOException {
        try {
            daprStopProcess = runCommandAndWaitUntil(DAPR_STOP_COMMAND, DAPR_STOP_PROCESS_TEXT_COMPLETED_TRIGGER);
            applicationStopProcess = runCommandAndWaitUntil(APPLICATION_STOP_COMMAND, APPLICATION_STOP_PROCESS_TEXT_COMPLETED_TRIGGER);
        } finally {
            closeProcess(daprProcess);
            closeProcess(daprStopProcess);
            closeProcess(applicationStopProcess);
        }
    }

    private void closeProcess(Process process) throws IOException {
        if (process != null) process.getOutputStream().close();
    }

    protected Process runCommandAndWaitUntil(String command, String endOfProcessTextTrigger) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while (!(line = in.readLine()).contains(endOfProcessTextTrigger)) {
                System.out.println(line);// TODO: consider using a logger instead
            }
            return p;
        } catch (IOException e) {
            if(p != null) p.destroy();
            throw new RuntimeException(e);
        }
    }

    protected int getDaprPort() {
        return 3500;
    }
}

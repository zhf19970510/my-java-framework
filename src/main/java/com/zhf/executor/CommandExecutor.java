package com.zhf.executor;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class CommandExecutor {

    public String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append("line").append("\n");
            }
            process.waitFor();
        } catch (Exception e) {
            log.error("CommandExecutor.executeCommand occurred error, command = {}", command, e);
        }
        return output.toString();
    }
}

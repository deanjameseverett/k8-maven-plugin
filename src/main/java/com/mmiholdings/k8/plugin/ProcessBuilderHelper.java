package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.logging.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessBuilderHelper {

    private final Log log;

    public ProcessBuilderHelper(Log l) {
        this.log = l;
    }

    public void executeCommand(String... command) throws IOException, InterruptedException {
        processs(null, Arrays.asList(command));
    }
    
    public void executeCommand(List<String> command) throws IOException, InterruptedException {
        processs(null, command);
    }

    public List<String> executeCommandWithResult(String... command) throws IOException, InterruptedException {
        return executeCommandWithResult(Arrays.asList(command));
    }
    
    public List<String> executeCommandWithResult(List<String> command) throws IOException, InterruptedException {
        List<String> l = new ArrayList<>();
        processs(null, command, l);
        return l;
    }

    public void executeCommand(File dockerDirectory, String... command) throws IOException, InterruptedException {
        executeCommand(dockerDirectory,Arrays.asList(command));
    }
    
    public void executeCommand(File dockerDirectory, List<String> command) throws IOException, InterruptedException {
        processs(dockerDirectory, command);
    }

    public boolean contains(File dockerDirectory, String fileName) {
        if(dockerDirectory.isDirectory()){
            File[] l = dockerDirectory.listFiles();
            for (int i = 0; i < dockerDirectory.listFiles().length; i++) {
                if (l[i].isFile()) {
                    if (l[i].getName().equals(fileName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void processs(File dockerDirectory, List<String> command, List<String> results) throws IOException {
        String line;
        BufferedReader br = execPS(dockerDirectory, command);
        while ((line = br.readLine()) != null) {
           results.add(line);
        }
    }

    private void processs(File dockerDirectory, List<String> command) throws IOException {
        BufferedReader br = execPS(dockerDirectory, command);
        String line;
        while ((line = br.readLine()) != null) {
            log.info(line);
        }
    }

    private BufferedReader execPS(File dockerDirectory, List<String> command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder().command(command);
        if (dockerDirectory != null) pb.directory(dockerDirectory);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        InputStream processStdOutput = process.getInputStream();
        Reader r = new InputStreamReader(processStdOutput);
        BufferedReader br = new BufferedReader(r);
        return br;
    }
}

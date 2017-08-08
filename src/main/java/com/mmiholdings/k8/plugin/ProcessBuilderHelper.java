package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.logging.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessBuilderHelper {

    private Log log;

    public ProcessBuilderHelper(Log l) {
        this.log = l;
    }

    public void executeCommand(List<String> command) throws IOException, InterruptedException {
        processs(null, command);
    }

    public List<String> executeCommandWithResult(List<String> command) throws IOException, InterruptedException {
        List<String> l = new ArrayList<String>();
        processs(null, command, l);
        return l;
    }


    public void executeCommand(String dockerDirectory, List<String> command) throws IOException, InterruptedException {
        processs(dockerDirectory, command);
    }

    private void processs(String dockerDirectory, List<String> command, List<String> results) throws IOException {
        String line;
        BufferedReader br = execPS(dockerDirectory, command);
        while ((line = br.readLine()) != null) {
           results.add(line);
        }
    }

    private void processs(String dockerDirectory, List<String> command) throws IOException {
        BufferedReader br = execPS(dockerDirectory, command);
        String line;
        while ((line = br.readLine()) != null) {
            log.info(line);
        }
    }

    private BufferedReader execPS(String dockerDirectory, List<String> command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder().command(command);
        if (dockerDirectory != null) pb.directory(new File(dockerDirectory));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        InputStream processStdOutput = process.getInputStream();
        Reader r = new InputStreamReader(processStdOutput);
        BufferedReader br = new BufferedReader(r);
        String line;
        return br;
    }

    public boolean contains(String dockerDirectory, String fileName) {
        File f = new File(dockerDirectory);
        File[] l = f.listFiles();
        for (int i = 0; i < f.listFiles().length; i++) {
            if (l[i].isFile()) {
                if (l[i].getName().equals(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }

}

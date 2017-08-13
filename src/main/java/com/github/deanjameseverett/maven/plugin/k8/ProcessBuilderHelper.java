package com.github.deanjameseverett.maven.plugin.k8;

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

    public void executeCommand(File directory, String... command) throws IOException, InterruptedException {
        executeCommand(directory,Arrays.asList(command));
    }
    
    public void executeCommand(File directory, List<String> command) throws IOException, InterruptedException {
        processs(directory, command);
    }

    public boolean contains(File directory, String fileName) {
        if(directory.isDirectory()){
            File[] l = directory.listFiles();
            for (int i = 0; i < directory.listFiles().length; i++) {
                if (l[i].isFile()) {
                    if (l[i].getName().equals(fileName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void processs(File directory, List<String> command, List<String> results) throws IOException {
        String line;
        BufferedReader br = execPS(directory, command);
        while ((line = br.readLine()) != null) {
           results.add(line);
        }
    }

    private void processs(File directory, List<String> command) throws IOException {
        BufferedReader br = execPS(directory, command);
        String line;
        while ((line = br.readLine()) != null) {
            log.info(line);
        }
    }

    private BufferedReader execPS(File directory, List<String> command) throws IOException {
        
        ProcessBuilder pb = new ProcessBuilder().command(command);
        if (directory == null) directory = getTmp(); 
        pb.directory(directory);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        InputStream processStdOutput = process.getInputStream();
        Reader r = new InputStreamReader(processStdOutput);
        BufferedReader br = new BufferedReader(r);
        return br;
    }

    private File getTmp() {
        return new File(tmpDir);
    }
    
    private static final String SH = "sh";
    private final String tmpDir = System.getProperty(TMP_DIR_PROP);
    private static final String TMP_DIR_PROP = "java.io.tmpdir";
}

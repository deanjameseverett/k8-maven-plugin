package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mojo(name = "deploy")
public class DeployK8Mojo extends AbstractMojo {

    public static final String PERSISTENCE_YML  = "persistence.yml";
    public static final String CLAIM_YML        = "claim.yml";
    public static final String DEPLOYMENT_YML   = "deployment.yml";
    public static final String SERVICE_YML      = "service.yml";

    ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());

    @Override
    public void execute() throws MojoExecutionException {
        
        getLog().info("Deploying component");
        try {
            File configDir = new File("/config");
            if (configDir.exists() && configDir.isDirectory()) {
                createConfig(configDir);
            }

            String s = Paths.get(".").toAbsolutePath().normalize().toString();
            String k8Directory = s + "/src/main/k8/";
            execute(k8Directory, PERSISTENCE_YML);
            execute(k8Directory, CLAIM_YML);
            execute(k8Directory, DEPLOYMENT_YML);
            execute(k8Directory, SERVICE_YML);
        } catch (InterruptedException | IOException e) {
            throw new MojoExecutionException("Could not deploy to Kubernetes",e);
        }
    }

    private void execute(String k8Directory, String command) throws InterruptedException, IOException {
        if (processBuilderHelper.contains(k8Directory, command)) {
            runCommand(command, k8Directory);
        }
    }



    private void runCommand(String cmd, String k8Directory) throws InterruptedException, IOException {
        List<String> command = new ArrayList<>();
        command.add("kubectl");
        command.add("create");
        command.add("-f");
        command.add(cmd);
        processBuilderHelper.executeCommand(k8Directory, command);
    }

    private void createConfig(File folder) throws InterruptedException, IOException {
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                List<String> cmd = configMapCommand(listOfFiles[i].getName());
                String str = Arrays.toString(cmd.toArray());
                getLog().info("config map command " + str);
            }
        }
    }


    private List<String> configMapCommand(String fileName) {
        List<String> command = new ArrayList<>();
        command.add("kubectl");
        command.add("create");
        command.add("configmap");
        command.add("fileName");
        command.add("--from-file=../../../config/" + fileName);
        return command;
    }
}

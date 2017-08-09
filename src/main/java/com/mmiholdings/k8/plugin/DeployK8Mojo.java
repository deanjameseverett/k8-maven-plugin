package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mojo(name = "deploy")
public class DeployK8Mojo extends AbstractKubernetesMojo {

    @Override
    public void execute() throws MojoExecutionException {
        if (kubernetesConfigmapExist()) {
            createConfig();
        }

        info("Deploying kubernetes components ....");
        runKubeControl(CREATE,persistenceFileName);
        runKubeControl(CREATE,claimFileName);
        runKubeControl(CREATE,deploymentFileName);
        runKubeControl(CREATE,serviceFileName);  
    }

    private void createConfig() {
        
        File folder = getKubernetesConfigFolder();
        File[] listOfFiles = folder.listFiles();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                List<String> cmd = configMapCommand(listOfFile.getAbsolutePath());
                String str = Arrays.toString(cmd.toArray());
                info("config map command " + str);
            }
        }
    }


    private List<String> configMapCommand(String fileName) {
        List<String> command = new ArrayList<>();
        command.add(KUBE_CONTROL);
        command.add(CREATE);
        command.add(CONFIG_MAP);
        command.add(FILENAME);
        command.add(MINUS_MINUS_FROM_FILE + fileName);
        return command;
    }
    
    
    private static final String CREATE = "create";
    private static final String CONFIG_MAP = "configmap";
    private static final String FILENAME = "fileName";
    private static final String MINUS_MINUS_FROM_FILE = "--from-file=";
        
}

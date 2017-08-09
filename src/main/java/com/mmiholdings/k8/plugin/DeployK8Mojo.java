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
        
        info("Deploying kubernetes components ....");
        
        File configDir = new File("/config");
        if (configDir.exists() && configDir.isDirectory()) {
            try {
                createConfig(configDir);
            } catch (InterruptedException | IOException e) {
                throw new MojoExecutionException("Could not create config map for Kubernetes",e);
            } 
        }

        runKubeControl(CREATE,persistenceFileName);
        runKubeControl(CREATE,claimFileName);
        runKubeControl(CREATE,deploymentFileName);
        runKubeControl(CREATE,serviceFileName);  
    }

    private void createConfig(File folder) throws InterruptedException, IOException {
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                List<String> cmd = configMapCommand(listOfFiles[i].getName());
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
        command.add("--from-file=../../../config/" + fileName);
        return command;
    }
    
    
    private static final String CREATE = "create";
    private static final String CONFIG_MAP = "configmap";
    private static final String FILENAME = "fileName";
        
}

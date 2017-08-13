package com.github.deanjameseverett.maven.plugin.k8.minikube;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Mojo(name = "startMinikube")
public class StartMinikubeMojo extends AbstractMinikubeMojo {

    @Override
    public void execute() throws MojoExecutionException {
        info("Checking state of minikube...");
        if(isRunning()){
            info("Minikube is already running");
        }else{
            info("Minikube is NOT running, starting now...");
            start();
        }
        info("Creating minikube docker environment");
    }

    private void start() throws MojoExecutionException{
        String[] cmd = { MINIKUBE, START};
        
        List<String> list = Arrays.asList(cmd);
        try {
            List<String> response = processBuilderHelper.executeCommandWithResult(list);
            response.forEach((responseLine) -> {
                info(responseLine + OK);
            });
        } catch (IOException | InterruptedException e) {
            throw new MojoExecutionException("Error while starting minikube",e);
        }
    }
    
    private static final String START = "start";
    
}

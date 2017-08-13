package com.github.deanjameseverett.maven.plugin.k8.minikube;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Mojo(name = "stopMinikube")
public class StopMinikubeMojo extends AbstractMinikubeMojo {

    @Override
    public void execute() throws MojoExecutionException {
        info("Checking state of minikube...");
        if(isRunning()){
            info("Minikube is running, stopping it now...");
            stop();
        }else{
            info("Minikube is NOT running");
        }
    }

    private void stop() throws MojoExecutionException{
        String[] cmd = { MINIKUBE, STOP };
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
    
    private static final String STOP = "stop";
   
}

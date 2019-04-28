package com.github.deanjameseverett.maven.plugin.k8.minikube;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mojo(name = "startMinikube")
public class StartMinikubeMojo extends AbstractMinikubeMojo {

    @Override
    public void execute() throws MojoExecutionException {
        info("Checking state of minikube...");
        if(isRunning()){
            info("Minikube is already running");
        }else{
            info("Minikube is NOT running, starting now...");
            if (hasArgs()) {
            	startWithArgs();
            } else {
            	start();
            }
        }
        info("Creating minikube docker environment");
    }

    private void start() throws MojoExecutionException{
        String[] cmd = { MINIKUBE, START};
        
        List<String> list = Arrays.asList(cmd);
        
        start(list);
    }
    
    private boolean hasArgs() {
    	return !Objects.isNull(insecureRegistry) || !Objects.isNull(memory) || !Objects.isNull(cpus);
    }
    
    private List<String> buildStartWithArgsCmd() {
    	List<String> cmdWithArgs = new ArrayList<String>(); 
    	cmdWithArgs.add(MINIKUBE);
    	cmdWithArgs.add(START);
    	
    	if (!Objects.isNull(insecureRegistry)) {
    		cmdWithArgs.add(ARG_INSECURE_REGISTRY + "='" + insecureRegistry + "'");
    		
    	}
    	if (!Objects.isNull(memory)) {
    		cmdWithArgs.add(ARG_MEMORY);
    		cmdWithArgs.add(String.valueOf(memory));
    	}
    	if (!Objects.isNull(cpus)) {
    		cmdWithArgs.add(ARG_CPUS);
    		cmdWithArgs.add(String.valueOf(cpus));
    	}
    	
    	return cmdWithArgs;
    }
    
    private void startWithArgs() throws MojoExecutionException {
        List<String> cmdWithArgs = buildStartWithArgsCmd();
        start(cmdWithArgs);
    }
    
    private void start(List<String> cmdList) throws MojoExecutionException{
        try {
            List<String> response = processBuilderHelper.executeCommandWithResult(cmdList);
            response.forEach((responseLine) -> {
                info(responseLine + OK);
            });
        } catch (IOException | InterruptedException e) {
            throw new MojoExecutionException("Error while starting minikube",e);
        }
    }    
    
    private static final String START = "start";
    private static final String ARG_INSECURE_REGISTRY = "--insecure-registry";
    private static final String ARG_MEMORY = "--memory";
    private static final String ARG_CPUS = "--cpus";
}


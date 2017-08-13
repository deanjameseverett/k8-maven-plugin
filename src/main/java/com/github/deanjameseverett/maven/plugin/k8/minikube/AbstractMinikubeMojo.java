package com.github.deanjameseverett.maven.plugin.k8.minikube;

import com.github.deanjameseverett.maven.plugin.k8.AbstractMojo;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * Abstract class for minikube mojos. 
 */
public abstract class AbstractMinikubeMojo extends AbstractMojo {
    protected static final String MINIKUBE = "minikube";
    protected static final String OK = " [OK]";
    protected static final String FAIL = " [NOT OK]";
    
    protected boolean isRunning(){
        String[] cmd = { MINIKUBE, STATUS };
        List<String> list = Arrays.asList(cmd);
        try {
            List<String> response = processBuilderHelper.executeCommandWithResult(list);
            for(String responseLine: response){
                if(responseLine.contains(RUNNING) || responseLine.contains(CORRECT)){
                    info(responseLine + OK);
                }else{
                    info(responseLine + FAIL);
                    return false;
                }
            }
            return true;
        } catch (IOException | InterruptedException e) {
            error("Error while checking the state of minikube",e);
            return false;
        }
    }

    private static final String STATUS = "status";
    private static final String RUNNING = "Running";
    private static final String CORRECT = "Correctly Configured";
    
}

package com.mmiholdings.k8.plugin.docker;

import com.mmiholdings.k8.plugin.ProcessBuilderHelper;
import static com.mmiholdings.k8.plugin.docker.AbstractDockerMojo.DOUBLE_DOT;
import java.io.File;
import java.io.IOException;

/**
 * Helping with Docker commands
 * @author Phillip Kruger (phillip.kruger@phillip-kruger.com)
 */
public class DockerCommandHelper {

    private final ProcessBuilderHelper processBuilderHelper;
    
    public DockerCommandHelper(ProcessBuilderHelper processBuilderHelper){
        this.processBuilderHelper = processBuilderHelper;
    }
    
    public void build(File baseDir,String imageName) throws InterruptedException, IOException {
        processBuilderHelper.executeCommand(baseDir, DOCKER,BUILD,MINUS_T,imageName,DOT);
    }
    
    public void delete(File baseDir,String imageName,String imageVersion) throws InterruptedException, IOException {
        String fullImageName = getFullyQualifiedImageName(imageName, imageVersion);
        processBuilderHelper.executeCommand(baseDir, DOCKER,RMI,MINUS_F,fullImageName);
    }
    
    private String getFullyQualifiedImageName(String imageName,String imageVersion){
        return imageName + DOUBLE_DOT + imageVersion;
    }
    
    private static final String DOT = ".";
    private static final String DOCKER = "docker";
    private static final String BUILD = "build";
    private static final String RMI = "rmi";
    private static final String MINUS_T = "-t";
    private static final String MINUS_F = "-f";
}

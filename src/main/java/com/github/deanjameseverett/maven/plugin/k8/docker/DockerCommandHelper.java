package com.github.deanjameseverett.maven.plugin.k8.docker;

import com.github.deanjameseverett.maven.plugin.k8.ProcessBuilderHelper;
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
   
    /**
     * @param baseDir : project.build.directory as defined in the projects pom.xml file
     * @param imageName : project.artifactId as defined in the project pom.xml file
     * @param imageVersion : project.version as defined in the project pom.xml file
     * @param dockerRegistry : project.properties.dockerRegistry as defined in the project pom.xml file
     * @throws InterruptedException : throws InterruptedException
     * @throws IOException : throws IOException
     * 
     * If dockerRegistry is not null the image will be tagged as dockerRegistry/imageName:imageVersion
     * If dockerRegistry is null the image will be tagged as imageName:imageVersion. 
     */
    public void build(File baseDir,String imageName,String imageVersion,String dockerRegistry) throws InterruptedException, IOException {
    	
    	String fullImageName = null;
    	
    	if(dockerRegistry != null && !dockerRegistry.isEmpty()) { 
    		fullImageName = getFullyQualifiedImageName(imageName, imageVersion, dockerRegistry);
    	} else {
    		fullImageName = getFullyQualifiedImageName(imageName, imageVersion);
    	}
    	
        processBuilderHelper.executeCommand(baseDir,DOCKER,BUILD,MINUS_T,fullImageName,DOT);
    }
    
    /**
     * @param baseDir : project.build.directory as defined in the projects pom.xml file
     * @param imageName : project.artifactId as defined in the project pom.xml file
     * @param imageVersion : project.version as defined in the project pom.xml file
     * @param dockerRegistry : project.properties.dockerRegistry as defined in the project pom.xml file
     * @throws InterruptedException : throws InterruptedException
     * @throws IOException : throws IOException
     * 
     * If dockerRegistry is not null the image will be push to the dockerRegistry
     * If dockerRegistry is null the image will not be push, this method will simply return having done nothing.
     */
    public void pushImage(File baseDir,String imageName,String imageVersion,String dockerRegistry) throws InterruptedException, IOException {
 	
    	if(dockerRegistry != null && !dockerRegistry.isEmpty()) { 
    		String fullImageName = getFullyQualifiedImageName(imageName, imageVersion, dockerRegistry);
    		processBuilderHelper.executeCommand(baseDir,DOCKER,PUSH,fullImageName);
    	}
         
    }
    
    @Deprecated
    public void delete(File baseDir,String imageName,String imageVersion) throws InterruptedException, IOException {
        String fullImageName = getFullyQualifiedImageName(imageName, imageVersion);
        processBuilderHelper.executeCommand(baseDir, DOCKER,RMI,MINUS_F,fullImageName);
    }
    
    public void delete(File baseDir,String imageName,String imageVersion, String dockerRegistry) throws InterruptedException, IOException {
    	
    	String fullImageName = null;
    	
    	if(dockerRegistry != null && !dockerRegistry.isEmpty()) { 
    		fullImageName = getFullyQualifiedImageName(imageName, imageVersion, dockerRegistry);
    	} else {
    		fullImageName = getFullyQualifiedImageName(imageName, imageVersion);
    	}    	
    	
        processBuilderHelper.executeCommand(baseDir, DOCKER,RMI,MINUS_F,fullImageName);
    }    
    
    private String getFullyQualifiedImageName(String imageName,String imageVersion){
        return imageName + DOUBLE_DOT + imageVersion;
    }
    
    /**
     * @param imageName : project.artifactId as defined in the project pom.xml file
     * @param imageVersion : project.version as defined in the project pom.xml file
     * @param dockerRegistry : project.properties.dockerRegistry as defined in the project pom.xml file
     * @return fully qualified image name including docker registry details in the format of dockerRegistry/imageName:imageVersion
     * 
     * If the String dockerRegistry ends with a "/" as configured in project.properties.dockerRegistry the "/" is removed before fully qualified image name is generated
     */
    private String getFullyQualifiedImageName(String imageName,String imageVersion, String dockerRegistry){
    	
    	if (dockerRegistry.substring(dockerRegistry.length()-1).equals(FORWARD_SLASH)) {
    		dockerRegistry = dockerRegistry.substring(0, dockerRegistry.length() - 1);
    	}
    	
        return dockerRegistry + FORWARD_SLASH + imageName + DOUBLE_DOT + imageVersion;
        
    }
    
    private static final String DOT = ".";
    private static final String DOCKER = "docker";
    private static final String BUILD = "build";
    private static final String PUSH = "push";
    private static final String RMI = "rmi";
    private static final String MINUS_T = "-t";
    private static final String MINUS_F = "-f";
    private static final String DOUBLE_DOT = ":";
    private static final String FORWARD_SLASH = "/";
}

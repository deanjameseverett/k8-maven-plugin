package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;


/**
 * Abstract class for docker mojos. Define some shared parameters
 * @see https://maven.apache.org/guides/plugin/guide-java-plugin-development.html
 */
public abstract class AbstractDockerMojo extends AbstractMojo {
    
    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required=false)
    protected File target;
    
    @Parameter( property = "imageName", defaultValue = "${project.artifactId}", readonly=true, required=false)
    protected String imageName;

    @Parameter( property = "imageVersion", defaultValue = "${project.version}", readonly=true, required=false)
    protected String imageVersion;

    @Parameter( property = "dockerConfDir", defaultValue = "${project.basedir}/src/main/docker", readonly=true, required=false)
    protected String dockerConfDir;
            
    @Parameter( property = "artefactName", defaultValue = "${project.build.finalName}", readonly=true, required=false)
    protected String artefactName;
    
    @Parameter( property = "artefactType", defaultValue = "${project.packaging}", readonly=true, required=false)
    protected String artefactType;
    
    @Parameter( property = "encoding", defaultValue = "${project.build.sourceEncoding}" )
    protected String encoding;
    
    protected final ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());
        
    protected void info(String msg){
        getLog().info(msg);
    }
    
    protected void error(String msg,Throwable e){
        getLog().error(msg,e);
    }
    
    
    protected boolean dockerfileExist(String dockerDirectory){
        return processBuilderHelper.contains(dockerDirectory, DOCKERFILE);// TODO: Make configuratable ?, or loop through all Dockerfiles?
    }
    
    protected String getFullyQualifiedImageName(){
        return imageName + DOUBLE_DOT + imageVersion;
    }
    
    protected static final String DOT = ".";
    protected static final String DOUBLE_DOT = ":";
    protected static final String DOCKER = "docker";
    protected static final String BUILD = "build";
    protected static final String RMI = "rmi";
    protected static final String MINUS_T = "-t";
    protected static final String MINUS_F = "-f";
    protected static final String DOCKERFILE = "Dockerfile";
    
}

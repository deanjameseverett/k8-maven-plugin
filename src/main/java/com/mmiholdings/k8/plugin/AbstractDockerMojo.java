package com.mmiholdings.k8.plugin;

import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;


/**
 * Abstract class for docker mojos. Define some shared parameters
 */
public abstract class AbstractDockerMojo extends AbstractMojo {
    
    @Parameter( property = "imageName", defaultValue = "${project.artifactId}", readonly=true, required=false)
    protected String imageName;

    @Parameter( property = "imageVersion", defaultValue = "${project.version}", readonly=true, required=false)
    protected String imageVersion;

    @Parameter( property = "dockerConfDir", defaultValue = "${project.basedir}/src/main/docker", readonly=true, required=false)
    protected String dockerConfDir;

    @Parameter( property = "artefactType", defaultValue = "${project.packaging}", readonly=true, required=false)
    protected String artefactType;
    
    @Parameter( property = "dockerFileName", defaultValue = "Dockerfile", readonly=true, required=false)
    protected String dockerFileName;
    
    protected boolean dockerfileExist(){
        return processBuilderHelper.contains(dockerConfDir, dockerFileName);
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
}

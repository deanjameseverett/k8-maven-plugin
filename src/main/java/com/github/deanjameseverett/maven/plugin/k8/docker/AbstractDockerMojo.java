package com.github.deanjameseverett.maven.plugin.k8.docker;

import com.github.deanjameseverett.maven.plugin.k8.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Abstract class for docker mojos. Define some shared parameters
 */
public abstract class AbstractDockerMojo extends AbstractMojo {
    
    @Parameter( property = "imageName", defaultValue = "${project.artifactId}", readonly=true, required=false)
    protected String imageName;

    @Parameter( property = "imageVersion", defaultValue = "${project.version}", readonly=true, required=false)
    protected String imageVersion;
    
    @Parameter( property = "dockerRegistry", defaultValue = "${project.properties.dockerRegistry}", readonly=true, required=false)
    protected String dockerRegistry;

    @Parameter( property = "dockerConfDir", defaultValue = "${project.basedir}/src/main/docker", readonly=true, required=false)
    protected String dockerConfDir;

    @Parameter( property = "artefactType", defaultValue = "${project.packaging}", readonly=true, required=false)
    protected String artefactType;
    
    @Parameter( property = "dockerFileName", defaultValue = "Dockerfile", readonly=true, required=false)
    protected String dockerFileName;
    
    protected boolean dockerfileExist(){
        return processBuilderHelper.contains(new File(dockerConfDir), dockerFileName);
    }
    
    protected DockerCommandHelper getDockerCommandHelper(){
        return new DockerCommandHelper(processBuilderHelper);
    }
    
    protected void copy() throws MojoExecutionException{
        super.copy(new File(dockerConfDir));
    }
}

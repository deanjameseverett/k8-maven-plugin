package com.mmiholdings.k8.plugin.docker;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;

import java.io.IOException;

/**
 * Maven Plug-in to build the docker image.
 * By default all file in docker dir will be copied and filtered to target folder, and will docker build from there. 
 */
@Mojo(name = "buildImage",defaultPhase = LifecyclePhase.INSTALL)
public class BuildDockerImageMojo extends AbstractDockerMojo {
    
    @Override
    public void execute() throws MojoExecutionException {
        info("Building Image using Dockerfile in [" + dockerConfDir + "]");
        
        try {
            if (dockerfileExist()) {
                // Copy the resource to target
                super.copy();
                // Execute Docker commands
                getDockerCommandHelper().build(target, imageName);
            }
        }  catch (IOException | InterruptedException ex) {
            throw new MojoExecutionException(ex.getMessage(),ex);
        }
    }

    
}
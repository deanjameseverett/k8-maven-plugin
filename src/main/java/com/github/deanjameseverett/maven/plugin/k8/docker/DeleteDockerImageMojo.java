package com.github.deanjameseverett.maven.plugin.k8.docker;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;
import org.apache.maven.plugins.annotations.LifecyclePhase;

/**
 * Maven Plug-in to remove the docker image.
 */
@Mojo(name = "deleteImage",defaultPhase = LifecyclePhase.INSTALL)
public class DeleteDockerImageMojo extends AbstractDockerMojo {
    
    @Override
    public void execute()throws MojoExecutionException {
        info("Deleting docker image [" + imageName +"]");
        
        try {
            if (dockerfileExist()) {
                // Copy the resource to target
                super.copy();
                // Execute Docker commands
                getDockerCommandHelper().delete(target, imageName, imageVersion);
            }
        }  catch (IOException | InterruptedException e) {
            // We do not fail... Just print in log
            error("Could not delete docker image [" + imageName + "]",e);
        }
    }

}
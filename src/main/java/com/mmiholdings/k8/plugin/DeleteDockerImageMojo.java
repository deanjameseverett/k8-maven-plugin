package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugins.annotations.LifecyclePhase;

/**
 * Maven Plug-in to remove the docker image.
 */
@Mojo(name = "deleteImage",defaultPhase = LifecyclePhase.INSTALL)
public class DeleteDockerImageMojo extends AbstractDockerMojo {
    
    @Override
    public void execute()throws MojoExecutionException {
        info("Deleting docker image [" + getFullyQualifiedImageName() +"]");
        
        try {
            if (dockerfileExist(dockerConfDir)) {
                List<String> command = new ArrayList<>();
                command.add(DOCKER);
                command.add(RMI);
                command.add(MINUS_F);
                command.add(getFullyQualifiedImageName());
                processBuilderHelper.executeCommand(dockerConfDir, command);
            }
        }  catch (IOException | InterruptedException e) {
            error("Could not delete docker image [" + getFullyQualifiedImageName() + "]",e);
        }
    }

}

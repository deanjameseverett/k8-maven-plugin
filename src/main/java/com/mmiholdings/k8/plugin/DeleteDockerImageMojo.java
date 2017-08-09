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
        info("Deleting Image [" + getFullyQualifiedImageName() +"]");
        execute(dockerConfDir, getFullyQualifiedImageName());
    }

    private void execute(String dockerDirectory, String imageName) {
        try {
            if (dockerfileExist(dockerDirectory)) {
                runDockerBuildCommand(dockerDirectory, imageName);
            }
        }  catch (IOException | InterruptedException e) {
            getLog().error(e);
        }
    }

    private void runDockerBuildCommand(String dockerDirectory, String imageName) throws InterruptedException, IOException {
        List<String> command = new ArrayList<>();
        command.add(DOCKER);
        command.add(RMI);
        command.add(MINUS_F);
        command.add(imageName);
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }

}

package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "deleteImage")
public class DeleteDockerImageMojo extends AbstractMojo {


    /**
     * imageName
     */
    @Parameter( property = "imageName", defaultValue = "Hello World!" )
    private String imageName;

    /**
     * imageVersion
     */
    @Parameter( property = "imageVersion", defaultValue = "Hello World!" )
    private String imageVersion;

    ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());

    public void execute()
            throws MojoExecutionException {

        getLog().info("Deleting Image");
        String s = Paths.get(".").toAbsolutePath().normalize().toString();
        String dockerDirectory = s + "/src/main/docker/";
        String fullyQualifiedImageName = imageName + ":" + imageVersion;
        execute(dockerDirectory, fullyQualifiedImageName);
    }

    private void execute(String dockerDirectory, String imageName) {
        try {
            if (processBuilderHelper.contains(dockerDirectory, "Dockerfile")) {
                runDockerBuildCommand(dockerDirectory, imageName);
            }
        }  catch (Exception e) {
            getLog().error(e);
        }
    }

    private void runDockerBuildCommand(String dockerDirectory, String imageName) throws InterruptedException, IOException {
        List<String> command = new ArrayList<String>();
        command.add("docker");
        command.add("rmi");
        command.add("-f");
        command.add(imageName);
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }

}

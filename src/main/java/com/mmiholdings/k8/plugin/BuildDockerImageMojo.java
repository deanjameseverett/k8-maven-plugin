package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "buildImage")
public class BuildDockerImageMojo extends AbstractMojo {


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


    @Parameter( property = "artefactName", defaultValue = "Hello World!" )
    private String artefactName;

    @Parameter( property = "artefactVersion", defaultValue = "Hello World!" )
    private String artefactVersion;


    ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());

    public void execute()
            throws MojoExecutionException
    {
        getLog().info("Building Image");
        String s = Paths.get(".").toAbsolutePath().normalize().toString();
        String dockerDirectory = s + "/src/main/docker/";
        String fullyQualifiedImageName = imageName + ":" + imageVersion;
        execute(dockerDirectory, fullyQualifiedImageName);
    }

    private void execute(String dockerDirectory, String imageName) {
        try {
            if (processBuilderHelper.contains(dockerDirectory, "Dockerfile")) {
                runCopyArtefactCommand(dockerDirectory);
                runDockerBuildCommand(dockerDirectory, imageName);
                runRemoveArtefactCommand(dockerDirectory);
            }
        }  catch (Exception e) {
            getLog().error(e);
        }
    }

    private void runCopyArtefactCommand(String dockerDirectory) throws InterruptedException, IOException {
        List<String> command = new ArrayList<String>();
        command.add("cp");
        command.add("../../../target/" + artefactName + "-" + artefactVersion + ".jar");
        command.add(".");
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }

    private void runRemoveArtefactCommand(String dockerDirectory) throws InterruptedException, IOException {
        List<String> command = new ArrayList<String>();
        command.add("rm");
        command.add(artefactName + "-" + artefactVersion + ".jar");
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }


    private void runDockerBuildCommand(String dockerDirectory, String imageName) throws InterruptedException, IOException {
        List<String> command = new ArrayList<String>();
        command.add("docker");
        command.add("build");
        command.add("-t");
        command.add(imageName);
        command.add(".");
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }

}

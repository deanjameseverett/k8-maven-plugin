package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "updateDeployment")
public class UpdateDeploymentK8Mojo extends AbstractMojo {


    public static final String DEPLOYMENT_YML   = "deployment.yml";

    ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());

    public void execute()
            throws MojoExecutionException
    {
        getLog().info("un Deploying component");
        String s = Paths.get(".").toAbsolutePath().normalize().toString();
        String dockerDirectory = s + "/src/main/k8/";
        execute(dockerDirectory, DEPLOYMENT_YML);
    }

    private void execute(String dockerDirectory, String command) {
        try {
            if (processBuilderHelper.contains(dockerDirectory, command)) {
                runCommand(command, dockerDirectory);
            }
        }  catch (Exception e) {
            getLog().error(e);
        }
    }

    private void runCommand(String cmd, String dockerDirectory) throws InterruptedException, IOException {
        List<String> command = new ArrayList<String>();
        command.add("kubectl");
        command.add("edit");
        command.add("-f");
        command.add(cmd);
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }

}

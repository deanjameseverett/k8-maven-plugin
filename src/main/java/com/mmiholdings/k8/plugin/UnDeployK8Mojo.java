package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "undeploy")
public class UnDeployK8Mojo extends AbstractMojo {

    public static final String PERSISTENCE_YML  = "persistence.yml";
    public static final String CLAIM_YML        = "claim.yml";
    public static final String DEPLOYMENT_YML   = "deployment.yml";
    public static final String SERVICE_YML      = "service.yml";

    private final ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("un Deploying component");
        String s = Paths.get(".").toAbsolutePath().normalize().toString();
        String dockerDirectory = s + "/src/main/k8/";

        execute(dockerDirectory, SERVICE_YML);
        execute(dockerDirectory, DEPLOYMENT_YML);
        execute(dockerDirectory, CLAIM_YML);
        execute(dockerDirectory, PERSISTENCE_YML);
    }

    private void execute(String dockerDirectory, String command) {
        try {
            if (processBuilderHelper.contains(dockerDirectory, command)) {
                runCommand(command, dockerDirectory);
            }
        }  catch (IOException | InterruptedException e) {
            getLog().error(e);
        }
    }

    private void runCommand(String cmd, String dockerDirectory) throws InterruptedException, IOException {
        List<String> command = new ArrayList<>();
        command.add("kubectl");
        command.add("delete");
        command.add("-f");
        command.add(cmd);
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }


}

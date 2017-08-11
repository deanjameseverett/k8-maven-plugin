package com.mmiholdings.k8.plugin.kubernetes;

import com.mmiholdings.k8.plugin.ProcessBuilderHelper;
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

    private final ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("un Deploying component");
        String s = Paths.get(".").toAbsolutePath().normalize().toString();
        String dockerDirectory = s + "/src/main/k8/";
        execute(new File(dockerDirectory), DEPLOYMENT_YML);
    }

    private void execute(File dockerDirectory, String command) {
        try {
            if (processBuilderHelper.contains(dockerDirectory, command)) {
                runCommand(command, dockerDirectory);
            }
        }  catch (IOException | InterruptedException e) {
            getLog().error(e);
        }
    }

    private void runCommand(String cmd, File dockerDirectory) throws InterruptedException, IOException {
        List<String> command = new ArrayList<>();
        command.add("kubectl");
        command.add("edit");
        command.add("-f");
        command.add(cmd);
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }

}

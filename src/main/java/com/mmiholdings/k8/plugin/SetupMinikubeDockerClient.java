package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mojo(name = "setup")
public class SetupMinikubeDockerClient extends AbstractMojo {

    ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());

    public void execute()
            throws MojoExecutionException
    {
        getLog().info("Setting up environment");
        ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());
        String[] cmd = { "minikube", "docker-env" };
        List<String> list = Arrays.asList(cmd);
        try {
            List<String> r = processBuilderHelper.executeCommandWithResult(list);
            for (String item : r) {
                getLog().info(item);
            }
        } catch (IOException e) {
            getLog().error(e);
        } catch (InterruptedException e) {
            getLog().error(e);
        }

    }


}

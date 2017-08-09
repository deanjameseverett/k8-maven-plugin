package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Mojo(name = "setup")
public class SetupMinikubeDockerClient extends AbstractMojo {

    private final ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Setting up environment");
        String[] cmd = { "minikube", "docker-env" };
        List<String> list = Arrays.asList(cmd);
        try {
            List<String> r = processBuilderHelper.executeCommandWithResult(list);
            r.forEach((item) -> {
                getLog().info(item);
            });
        } catch (IOException | InterruptedException e) {
            getLog().error(e);
        }

    }


}

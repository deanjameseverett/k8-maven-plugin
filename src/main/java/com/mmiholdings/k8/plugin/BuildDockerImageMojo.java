package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Maven Plug-in to build the docker image.
 * @see https://maven.apache.org/guides/plugin/guide-java-plugin-development.html
 */
@Mojo(name = "buildImage",defaultPhase = LifecyclePhase.INSTALL)
public class BuildDockerImageMojo extends AbstractDockerMojo {
    
    private String deployableUnit;
    
    @Override
    public void execute() throws MojoExecutionException {
        info("Building Image using Dockerfile in [" + dockerConfDir + "]");
        
        File f = new File(target,artefactName + DOT + artefactType);
        this.deployableUnit = f.getAbsolutePath();
        info("Including deployable unit [" + deployableUnit + "]");
        
        execute(dockerConfDir, getFullyQualifiedImageName());
    }

    private void execute(String dockerDirectory, String imageName) {
        try {
            if (dockerfileExist(dockerDirectory)) {
                runCopyArtefactCommand(dockerDirectory);
                runDockerBuildCommand(dockerDirectory, imageName);
                runRemoveArtefactCommand(dockerDirectory);
            }
        }  catch (IOException | InterruptedException e) {
            getLog().error(e);
        }
    }

    private void runCopyArtefactCommand(String dockerDirectory) throws InterruptedException, IOException {
        List<String> command = new ArrayList<>();
        command.add(COPY);
        command.add(deployableUnit);
        command.add(DOT);
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }

    private void runRemoveArtefactCommand(String dockerDirectory) throws InterruptedException, IOException {
        List<String> command = new ArrayList<>();
        command.add(REMOVE);
        command.add(artefactName + DOT + artefactType);
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }

    private void runDockerBuildCommand(String dockerDirectory, String imageName) throws InterruptedException, IOException {
        List<String> command = new ArrayList<>();
        command.add(DOCKER);
        command.add(BUILD);
        command.add(MINUS_T);
        command.add(imageName);
        info("Executing [docker build -t " + imageName + "]");
        command.add(DOT);
        processBuilderHelper.executeCommand(dockerDirectory, command);
    }
}

package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
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
public class BuildDockerImageMojo extends AbstractMojo {
    
    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required=false)
    private File target;
    
    @Parameter( property = "imageName", defaultValue = "${project.artifactId}", readonly=true, required=false)
    private String imageName;

    @Parameter( property = "imageVersion", defaultValue = "${project.version}", readonly=true, required=false)
    private String imageVersion;

    @Parameter( property = "dockerConfDir", defaultValue = "${project.basedir}/src/main/docker", readonly=true, required=false)
    private String dockerConfDir;
            
    @Parameter( property = "artefactName", defaultValue = "${project.build.finalName}", readonly=true, required=false)
    private String artefactName;
    
    @Parameter( property = "artefactType", defaultValue = "${project.packaging}", readonly=true, required=false)
    private String artefactType;
    
    private final ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());
    private String deployableUnit;
    
    @Override
    public void execute() throws MojoExecutionException {
        info("Building Image using Dockerfile in [" + dockerConfDir + "]");
        
        File f = new File(target,artefactName + DOT + artefactType);
        this.deployableUnit = f.getAbsolutePath();
        info("Including deployable unit [" + deployableUnit + "]");
        
        String fullyQualifiedImageName = imageName + DOUBLE_DOT + imageVersion;
        execute(dockerConfDir, fullyQualifiedImageName);
    }

    private void execute(String dockerDirectory, String imageName) {
        try {
            if (processBuilderHelper.contains(dockerDirectory, DOCKERFILE)) { // TODO: Make configuratable ?, or loop through all Dockerfiles?
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
    
    private void info(String msg){
        getLog().info(msg);
    }
    
    private static final String DOT = ".";
    private static final String DOUBLE_DOT = ":";
    private static final String COPY = "cp"; // TODO: Is this Linux specific ? What about poor Windoze users ?
    private static final String REMOVE = "rm"; // TODO: Is this Linux specific ? What about poor Windoze users ?
    private static final String DOCKER = "docker";
    private static final String BUILD = "build";
    private static final String MINUS_T = "-t";
    private static final String DOCKERFILE = "Dockerfile";
}

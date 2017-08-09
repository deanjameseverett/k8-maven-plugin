package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;

/**
 * Maven Plug-in to build the docker image.
 * @see https://maven.apache.org/guides/plugin/guide-java-plugin-development.html
 */
@Mojo(name = "buildImage",defaultPhase = LifecyclePhase.INSTALL)
public class BuildDockerImageMojo extends AbstractDockerMojo {
    
    private String deployableUnit;
    
    @Parameter(defaultValue="${project}",required=true, readonly=true)
    protected MavenProject project;
    
    @Parameter( defaultValue = "${session}", required = true, readonly = true )
    protected MavenSession session;
    
    @Component( role=MavenResourcesFiltering.class, hint="default")
    protected MavenResourcesFiltering mavenResourcesFiltering;

    @Parameter
    private List<String> dockerImageResources;
    
    @Override
    public void execute() throws MojoExecutionException {
        info("Building Image using Dockerfile in [" + dockerConfDir + "]");
        
        File f = new File(target,artefactName + DOT + artefactType);
        this.deployableUnit = f.getAbsolutePath();
        info("Including deployable unit [" + deployableUnit + "]");
        
        try {
            if (dockerfileExist(dockerConfDir)) {
                filterDockerfile();
                runDockerBuildCommand();
            }
        }  catch (IOException | InterruptedException ex) {
            throw new MojoExecutionException(ex.getMessage(),ex);
        }
    }

    /*
    * Here copy the Dockerfile to target folder, and add filtering. So all pom vars will be substituted.
    */
    private void filterDockerfile() throws MojoExecutionException{
        try {
            MavenResourcesExecution mre = new MavenResourcesExecution(getResources(), new File(dockerConfDir), this.project, this.encoding, new ArrayList<>(), new ArrayList<>(), session);
            mavenResourcesFiltering.filterResources(mre);
        } catch (MavenFilteringException ex) {
            throw new MojoExecutionException(ex.getMessage(),ex);
        }
    }
    
    private void runDockerBuildCommand() throws InterruptedException, IOException {
        List<String> command = new ArrayList<>();
        command.add(DOCKER);
        command.add(BUILD);
        command.add(MINUS_T);
        command.add(getFullyQualifiedImageName());
        info("Executing [docker build -t " + imageName + "]");
        command.add(DOT);
        processBuilderHelper.executeCommand(target.getAbsolutePath(), command);
    }
    
    private List<Resource> getResources(){
        List<Resource> resources = new ArrayList<>();
        // Add (by default) Dockerfile
        resources.add(createResource(DOCKERFILE));
        
        // Add any other as defined by the user
        if(dockerImageResources!=null && !dockerImageResources.isEmpty()){
            dockerImageResources.forEach((userDefinedResource) -> {
                resources.add(createResource(userDefinedResource));
            });
        }
        
        return resources;
    }
    
    private Resource createResource(String name){ 
        Resource resource = new Resource();
        resource.addInclude(name);
        resource.setDirectory(dockerConfDir);
        resource.setTargetPath(target.getAbsolutePath());
        resource.setFiltering(true);
        return resource;
    }
}

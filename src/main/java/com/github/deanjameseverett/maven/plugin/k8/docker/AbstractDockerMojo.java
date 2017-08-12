package com.github.deanjameseverett.maven.plugin.k8.docker;

import com.github.deanjameseverett.maven.plugin.k8.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Abstract class for docker mojos. Define some shared parameters
 */
public abstract class AbstractDockerMojo extends AbstractMojo {
    
    @Parameter( property = "imageName", defaultValue = "${project.artifactId}", readonly=true, required=false)
    protected String imageName;

    @Parameter( property = "imageVersion", defaultValue = "${project.version}", readonly=true, required=false)
    protected String imageVersion;

    @Parameter( property = "dockerConfDir", defaultValue = "${project.basedir}/src/main/docker", readonly=true, required=false)
    protected String dockerConfDir;

    @Parameter( property = "artefactType", defaultValue = "${project.packaging}", readonly=true, required=false)
    protected String artefactType;
    
    @Parameter( property = "dockerFileName", defaultValue = "Dockerfile", readonly=true, required=false)
    protected String dockerFileName;
    
    @Parameter
    private List<String> includeFileTypes;
    
    protected boolean dockerfileExist(){
        return processBuilderHelper.contains(new File(dockerConfDir), dockerFileName);
    }
    
    protected DockerCommandHelper getDockerCommandHelper(){
        return new DockerCommandHelper(processBuilderHelper);
    }
    
    protected void copy() throws MojoExecutionException{
        super.copy(new File(dockerConfDir), getIncludes());
    }
    
    protected List<String> getIncludes(){
        List<String> includes = new ArrayList<>();
        // Add (by default) Dockerfile
        info("... including " + dockerFileName);
        includes.add(dockerFileName);
        
        if(includeFileTypes==null || includeFileTypes.isEmpty()){
            includes.add(ALL_DOT_ALL);
        }else{
            includeFileTypes.forEach((userDefinedFileType) -> {
                info("... including " + userDefinedFileType);
                includes.add(ALL_DOT + userDefinedFileType);
            });
        }
        
        return includes;
    }
    
    private static final String ALL_DOT = "*.";
    private static final String ALL_DOT_ALL = "*.*";
}

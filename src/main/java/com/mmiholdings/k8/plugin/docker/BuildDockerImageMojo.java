package com.mmiholdings.k8.plugin.docker;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Maven Plug-in to build the docker image.
 * By default all file in docker dir will be copied and filtered to target folder, and will docker build from there. 
 */
@Mojo(name = "buildImage",defaultPhase = LifecyclePhase.INSTALL)
public class BuildDockerImageMojo extends AbstractDockerMojo {
    
    @Parameter
    private List<String> includeFileTypes;
    
    @Override
    public void execute() throws MojoExecutionException {
        info("Building Image using Dockerfile in [" + dockerConfDir + "]");
        
        try {
            if (dockerfileExist()) {
                // Copy the resource to target
                super.copy(new File(dockerConfDir), getIncludes());
                // Execute Docker commands
                getDockerCommandHelper().build(target, imageName);
            }
        }  catch (IOException | InterruptedException ex) {
            throw new MojoExecutionException(ex.getMessage(),ex);
        }
    }

    private List<String> getIncludes(){
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

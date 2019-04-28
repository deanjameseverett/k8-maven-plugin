package com.github.deanjameseverett.maven.plugin.k8.kubernetes;

import java.io.IOException;
import java.util.Objects;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "apply")
public class ApplyK8Mojo extends AbstractKubernetesMojo {

    @Override
    public void execute() throws MojoExecutionException {
        info("Applying kubernetes components ....");
        
        try {
            if (kubernetesFolderExist()) {
                // Copy the resource to target
                super.copy();
                // Execute Docker commands
                Objects.requireNonNull(includeFiles, "When using 'apply', list of files must be specified via 'includeFiles' configuration property!");
                getKubernetesCommandHelper().apply(target,includeFiles.toArray(new String[includeFiles.size()]));
            }
        }  catch (IOException | InterruptedException ex) {
            throw new MojoExecutionException(ex.getMessage(),ex);
        }
        
    }
}

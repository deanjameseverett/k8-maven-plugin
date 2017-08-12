package com.github.deanjameseverett.maven.plugin.k8.kubernetes;

import java.io.IOException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "deploy")
public class DeployK8Mojo extends AbstractKubernetesMojo {

    @Override
    public void execute() throws MojoExecutionException {
        info("Deploying kubernetes components ....");
        
        try {
            if (kubernetesFolderExist()) {
                // Copy the resource to target
                super.copy();
                // Execute Docker commands
                getKubernetesCommandHelper().create(target,persistenceFileName,claimFileName,deploymentFileName,serviceFileName);
            }
        }  catch (IOException | InterruptedException ex) {
            throw new MojoExecutionException(ex.getMessage(),ex);
        }
        
    }
    
    
}

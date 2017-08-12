package com.mmiholdings.k8.plugin.kubernetes;

import java.io.IOException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "updateDeployment")
public class UpdateDeploymentK8Mojo extends AbstractKubernetesMojo {

    @Override
    public void execute() throws MojoExecutionException {
        info("Updating kubernetes components ....");
        
        try {  
            if (kubernetesFolderExist()) {
                // Copy the resource to target
                super.copy();
                // Execute Docker commands
                getKubernetesCommandHelper().update(target,deploymentFileName);                
            }
        } catch (InterruptedException | IOException ex) {
            throw new MojoExecutionException("Error while creating Kubernetes environment",ex);
        }
    }
}

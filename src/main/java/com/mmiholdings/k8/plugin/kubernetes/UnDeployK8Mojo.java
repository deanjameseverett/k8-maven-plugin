package com.mmiholdings.k8.plugin.kubernetes;

import java.io.IOException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "undeploy")
public class UnDeployK8Mojo extends AbstractKubernetesMojo {

    @Override
    public void execute() throws MojoExecutionException {
        info("Undeploying kubernetes components ....");
        
        try {
            if (kubernetesFolderExist()) {
                // Copy the resource to target
                super.copy();
                // Execute Docker commands
                getKubernetesCommandHelper().delete(target,persistenceFileName,claimFileName,deploymentFileName,serviceFileName);
            }
        }  catch (IOException | InterruptedException e) {
            // We do not fail... Just print in log
            error("Could not undeploy kubernetes components",e);
        }
    }

}

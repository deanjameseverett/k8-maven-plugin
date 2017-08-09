package com.mmiholdings.k8.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "undeploy")
public class UnDeployK8Mojo extends AbstractKubernetesMojo {

    @Override
    public void execute() throws MojoExecutionException {
        info("Undeploying kubernetes components ....");
        if (kubernetesConfigmapExist()) {
            deleteConfigMap(artefactName);
        }
        runKubeControl(DELETE,serviceFileName);
        runKubeControl(DELETE,deploymentFileName);
        runKubeControl(DELETE,claimFileName);
        runKubeControl(DELETE,persistenceFileName);

    }

}

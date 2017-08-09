package com.mmiholdings.k8.plugin;

import static com.mmiholdings.k8.plugin.AbstractMojo.MINUS_F;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Abstract class for kubernetes mojos. Define some shared parameters
 * @see https://maven.apache.org/guides/plugin/guide-java-plugin-development.html
 */
public abstract class AbstractKubernetesMojo extends AbstractMojo {
    
    @Parameter( property = "kubernetesConfDir", defaultValue = "${project.basedir}/src/main/k8", readonly=true, required=false)
    protected String kubernetesConfDir;
    
    @Parameter( property = "persistenceFileName", defaultValue = "persistence.yml", readonly=true, required=false)
    protected String persistenceFileName;
    
    @Parameter( property = "claimFileName", defaultValue = "claim.yml", readonly=true, required=false)
    protected String claimFileName;
    
    @Parameter( property = "deploymentFileName", defaultValue = "deployment.yml", readonly=true, required=false)
    protected String deploymentFileName;
    
    @Parameter( property = "serviceFileName", defaultValue = "service.yml", readonly=true, required=false)
    protected String serviceFileName;
    
    protected boolean kubernetesfileExist(String filename){
        return processBuilderHelper.contains(kubernetesConfDir, filename);
    }
    
    protected void runKubeControl(String instruction,String filename) throws MojoExecutionException {
        if (kubernetesfileExist(filename)) {
            List<String> command = new ArrayList<>();
            command.add(KUBE_CONTROL);
            command.add(instruction);
            command.add(MINUS_F);
            command.add(filename);
            try {
                processBuilderHelper.executeCommand(kubernetesConfDir, command);
            } catch (IOException | InterruptedException e) {
                throw new MojoExecutionException("Could not " + instruction + " from Kubernetes [" + filename + "]",e);
            }
        }
    }
    
    protected static final String KUBE_CONTROL = "kubectl";
    
}

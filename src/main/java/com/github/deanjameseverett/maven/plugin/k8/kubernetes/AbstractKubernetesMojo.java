package com.github.deanjameseverett.maven.plugin.k8.kubernetes;

import com.github.deanjameseverett.maven.plugin.k8.AbstractMojo;
import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Abstract class for kubernetes mojos. Define some shared parameters
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
    
    protected boolean kubernetesFolderExist(){
        return new File(kubernetesConfDir).exists();
    }
    
    protected KubernetesCommandHelper getKubernetesCommandHelper(){
        return new KubernetesCommandHelper(processBuilderHelper,artefactName);
    }
    
    protected void copy() throws MojoExecutionException{
        super.copy(new File(kubernetesConfDir));
    }
}

package com.mmiholdings.k8.plugin.kubernetes;

import com.mmiholdings.k8.plugin.AbstractMojo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        super.copy(new File(kubernetesConfDir), getIncludes());
    }
    
    protected List<String> getIncludes(){
        List<String> includes = new ArrayList<>();
        // Add (by default) persistence.yml
        info("... including " + persistenceFileName);
        includes.add(persistenceFileName);
        // Add (by default) claim.yml
        info("... including " + claimFileName);
        includes.add(claimFileName);
        // Add (by default) deployment.yml
        info("... including " + deploymentFileName);
        includes.add(deploymentFileName);
        // Add (by default) service.yml
        info("... including " + serviceFileName);
        includes.add(serviceFileName);
        // Add (by default) all files in /config
        info("... including config/**.*");
        includes.add("config/*.*");
        
        return includes;
    }
}

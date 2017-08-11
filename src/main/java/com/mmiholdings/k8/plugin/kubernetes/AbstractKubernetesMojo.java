package com.mmiholdings.k8.plugin.kubernetes;

import com.mmiholdings.k8.plugin.AbstractMojo;
import java.io.File;
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

    protected static final String KUBE_CONTROL = "kubectl";
    protected static final String CONFIG_DIR = "/config";
    protected static final String CREATE = "create";
    protected static final String CONFIG_MAP = "configmap";
    protected static final String MINUS_MINUS_FROM_FILE = "--from-file=";
    protected static final String DELETE = "delete";

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
        return processBuilderHelper.contains(new File(kubernetesConfDir), filename);
    }
    
    protected File getKubernetesConfigFolder(){
        return new File(kubernetesConfDir + CONFIG_DIR);
    }
    
    protected boolean kubernetesConfigmapExist(){
        File configDir = getKubernetesConfigFolder();
        return configDir.exists() && configDir.isDirectory();
    }
    
    protected void runKubeControl(String instruction,String filename) throws MojoExecutionException {
        if (kubernetesfileExist(filename)) {
            List<String> command = new ArrayList<>();
            command.add(KUBE_CONTROL);
            command.add(instruction);
            command.add("-f");
            command.add(filename);
            try {
                processBuilderHelper.executeCommand(new File(kubernetesConfDir), command);
            } catch (IOException | InterruptedException e) {
                throw new MojoExecutionException("Could not " + instruction + " from Kubernetes [" + filename + "]",e);
            }
        }
    }


    protected void runKubeConfigMap(String configName, String fileName) throws MojoExecutionException {
        List<String> command = new ArrayList<>();
        command.add(KUBE_CONTROL);
        command.add(CREATE);
        command.add(CONFIG_MAP);
        command.add(configName);
        command.add(MINUS_MINUS_FROM_FILE + fileName);
        try {
            processBuilderHelper.executeCommand(new File(kubernetesConfDir), command);
        } catch (IOException | InterruptedException e) {
            throw new MojoExecutionException("Could not create configmap from Kubernetes [" + configName + "]",e);
        }
    }

    protected void deleteConfigMap(String configName) throws MojoExecutionException {
        List<String> command = new ArrayList<>();
        command.add(KUBE_CONTROL);
        command.add(DELETE);
        command.add(CONFIG_MAP);
        command.add(configName);
        try {
            processBuilderHelper.executeCommand(new File(kubernetesConfDir), command);
        } catch (IOException | InterruptedException e) {
            throw new MojoExecutionException("Could not create configmap from Kubernetes [" + configName + "]",e);
        }
    }


}

package com.mmiholdings.k8.plugin.kubernetes;

import com.mmiholdings.k8.plugin.ProcessBuilderHelper;
import java.io.File;
import java.io.IOException;

/**
 * Helping with Kubernetes commands
 * @author Phillip Kruger (phillip.kruger@phillip-kruger.com)
 */
public class KubernetesCommandHelper {

    private final ProcessBuilderHelper processBuilderHelper;
    private final String artefactName;
    
    public KubernetesCommandHelper(ProcessBuilderHelper processBuilderHelper,String artefactName){
        this.processBuilderHelper = processBuilderHelper;
        this.artefactName = artefactName;
    }
    
    public void create(File kubernetesConfDir,String... yamlFiles) throws InterruptedException, IOException {
        // Configmap
        if (kubernetesConfigmapExist(kubernetesConfDir)) {
            createConfigs(kubernetesConfDir);
        }
        
        // Create
        for(String yamlFile:yamlFiles){
            if(kubernetesfileExist(kubernetesConfDir, yamlFile)){
                processBuilderHelper.executeCommand(kubernetesConfDir, KUBE_CONTROL, CREATE, MINUS_F, yamlFile);
            }
        }
    }
    
    public void update(File kubernetesConfDir, String... yamlFiles) throws IOException, InterruptedException{
        for(String yamlFile:yamlFiles){
            if(kubernetesfileExist(kubernetesConfDir, yamlFile)){
                processBuilderHelper.executeCommand(kubernetesConfDir, KUBE_CONTROL, EDIT, MINUS_F, yamlFile);
            }
        }
    }
    
    public void delete(File kubernetesConfDir,String... yamlFiles) throws InterruptedException, IOException {
        // Delete
        for(String yamlFile:yamlFiles){
            if(kubernetesfileExist(kubernetesConfDir, yamlFile)){
                processBuilderHelper.executeCommand(kubernetesConfDir, KUBE_CONTROL, DELETE, MINUS_F, yamlFile);
            }
        }
        
        // Configmap
        if (kubernetesConfigmapExist(kubernetesConfDir)) {
            deleteConfigs(kubernetesConfDir);
        }
        
    }
    
    private boolean kubernetesfileExist(File kubernetesConfDir,String filename){
        return processBuilderHelper.contains(kubernetesConfDir, filename);
    }
    
    private boolean kubernetesConfigmapExist(File kubernetesConfDir){
        File configDir = getKubernetesConfigFolder(kubernetesConfDir);
        return configDir.exists() && configDir.isDirectory();
    }
    
    private File getKubernetesConfigFolder(File kubernetesConfDir){
        return new File(kubernetesConfDir,CONFIG);
    }
    
    private void createConfigs(File kubernetesConfDir) throws IOException, InterruptedException {
        File folder = getKubernetesConfigFolder(kubernetesConfDir);
        if (folder.list().length>0) {
            for(String fileName: folder.list()){
                processBuilderHelper.executeCommand(kubernetesConfDir,KUBE_CONTROL,CREATE,CONFIG_MAP,artefactName,MINUS_MINUS_FROM_FILE + fileName);
            }
        }
    }
    
    private void deleteConfigs(File kubernetesConfDir) throws IOException, InterruptedException {
        File folder = getKubernetesConfigFolder(kubernetesConfDir);
        if (folder.list().length>0) {
            processBuilderHelper.executeCommand(kubernetesConfDir,KUBE_CONTROL,DELETE,CONFIG_MAP,artefactName);
        }
    }
    
    private static final String KUBE_CONTROL = "kubectl";
    private static final String CONFIG = "config";
    private static final String CREATE = "create";
    private static final String EDIT = "edit";
    private static final String CONFIG_MAP = "configmap";
    private static final String MINUS_MINUS_FROM_FILE = "--from-file=";
    private static final String DELETE = "delete";
    private static final String MINUS_F = "-f";
}

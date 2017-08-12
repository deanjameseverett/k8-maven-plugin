package com.github.deanjameseverett.maven.plugin.k8;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;

/**
 * Help with copy of file, including filtering
 * @author Phillip Kruger (phillip.kruger@phillip-kruger.com)
 */
public class ResourceCopyHelper {

    private final MavenResourcesFiltering mavenResourcesFiltering;
    private final String encoding;
    private final MavenProject project;
    private final MavenSession session;
    
    public ResourceCopyHelper(MavenResourcesFiltering mavenResourcesFiltering,MavenProject project,MavenSession session,String encoding){
        this.mavenResourcesFiltering = mavenResourcesFiltering;
        this.encoding = encoding;
        this.project = project;
        this.session = session;
    }
    
    public void copy(File fromDir,File toDir,List<String> include) throws MojoExecutionException {
        try {
            MavenResourcesExecution mre = new MavenResourcesExecution(toResourceList(fromDir,toDir,include), fromDir, this.project, this.encoding, new ArrayList<>(), new ArrayList<>(), session);
            mavenResourcesFiltering.filterResources(mre);
        } catch (MavenFilteringException ex) {
            throw new MojoExecutionException(ex.getMessage(),ex);
        }
    }
    
    private List<Resource> toResourceList(File fromDir, File toDir,List<String> include){
        List<Resource> resouceList = new ArrayList<>();
        include.forEach((name) -> {
            resouceList.add(createResource(fromDir, toDir, name));
        });
        return resouceList;
    }
    
    private Resource createResource(File fromDir, File toDir, String name){ 
        Resource resource = new Resource();
        resource.addInclude(name);
        resource.setDirectory(fromDir.getAbsolutePath());
        resource.setTargetPath(toDir.getAbsolutePath());
        resource.setFiltering(true);
        return resource;
    }
}

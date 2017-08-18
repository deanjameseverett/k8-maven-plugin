package com.github.deanjameseverett.maven.plugin.k8;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    
    public void copy(File fromDir,File toDir,List<String> include,List<String> noFilterTypes) throws MojoExecutionException {
        // Get the current file rights
        Map<String, FileRights> fileRights = getFileRights(fromDir);
        
        // Add some default include
        if(include==null || include.isEmpty())include = DEFAULT_INCLUDES;
        // Add some defaults that we know about
        if(noFilterTypes==null || noFilterTypes.isEmpty())noFilterTypes = new ArrayList<>();
        noFilterTypes.addAll(DEFAULT_NO_FILTER_TYPES);
        
        try {
            MavenResourcesExecution mre = new MavenResourcesExecution(toResourceList(fromDir,toDir,include), fromDir, this.project, this.encoding, new ArrayList<>(),noFilterTypes, session);
            mavenResourcesFiltering.filterResources(mre);
            
            // Restore the file rights 
            restoreFileRights(fileRights,toDir);
            
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

    private void restoreFileRights(Map<String,FileRights> fileRights, File path ) {

        Set<Map.Entry<String, FileRights>> entrySet = fileRights.entrySet();

        entrySet.forEach((e) -> {
            File f = new File(path,e.getKey());
            FileRights fr = e.getValue();
            f.setReadable(fr.isRead());
            f.setWritable(fr.isWrite());
            f.setExecutable(fr.isExecute());
        });
        
    }
    
    private Map<String,FileRights> getFileRights(File filePath){
        Map<String,FileRights> m = new HashMap<>();
        String root = filePath.getAbsolutePath();
        getFileRights(m, filePath, root);
        return m;
    }
    
    private void getFileRights(Map<String,FileRights> m, File path ,String root) {
        
        File[] list = path.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                getFileRights(m, f ,root);
            }else {
                
                String fullPath = f.getAbsolutePath();
                String name = fullPath.substring(root.length());
                m.put(name, new FileRights(f.canRead(), f.canWrite(), f.canExecute()));
            }
        }
    }
    
    class FileRights {
        boolean read;
        boolean write;
        boolean execute;

        public FileRights(boolean read, boolean write, boolean execute) {
            this.read = read;
            this.write = write;
            this.execute = execute;
        }

        public boolean isRead() {
            return read;
        }

        public boolean isWrite() {
            return write;
        }

        public boolean isExecute() {
            return execute;
        }
        
    }        
            
    private static final List<String> DEFAULT_NO_FILTER_TYPES = Arrays.asList(new String[]{"deb","rpm","tar","gz","tar.gz","zip","war","ear","jar","rar", "jpg", "jpeg", "gif", "bmp","png","pdf"});
    private static final List<String> DEFAULT_INCLUDES = Arrays.asList(new String[]{"*","*.*","**/*","**/*.*"}); 
}

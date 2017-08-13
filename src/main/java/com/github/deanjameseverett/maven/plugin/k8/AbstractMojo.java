package com.github.deanjameseverett.maven.plugin.k8;

import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;


/**
 * Abstract class for our own mojos. Define some shared parameters
 * @see https://maven.apache.org/guides/plugin/guide-java-plugin-development.html
 */
public abstract class AbstractMojo extends org.apache.maven.plugin.AbstractMojo {
    
    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required=false)
    protected File target;
    
    @Parameter( property = "encoding", defaultValue = "${project.build.sourceEncoding}" )
    protected String encoding;

    @Parameter( property = "artefactName", defaultValue = "${project.build.finalName}", readonly=true, required=false)
    protected String artefactName;

    @Parameter(defaultValue="${project}",required=true, readonly=true)
    protected MavenProject project;
    
    @Parameter( defaultValue = "${session}", required = true, readonly = true )
    protected MavenSession session;
    
    @Component( role=MavenResourcesFiltering.class, hint="default")
    protected MavenResourcesFiltering mavenResourcesFiltering;
    
    protected final ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());
    
    
    protected void copy(File fromDir,String... include) throws MojoExecutionException {
        copy(fromDir,Arrays.asList(include));
    }
    
    protected void copy(File fromDir,List<String> include) throws MojoExecutionException {
        copy(fromDir,target,include);
    }
    
    protected void copy(File fromDir,File toDir,String... include) throws MojoExecutionException {
        copy(fromDir,toDir,Arrays.asList(include));
    }
    
    protected void copy(File fromDir,File toDir,List<String> include) throws MojoExecutionException {
        getResourceCopyHelper().copy(fromDir, toDir, include);
    }
    
    protected void info(String msg){
        getLog().info(msg);
    }

    protected void error(String msg){
        getLog().error(msg);
    }
    
    protected void error(String msg,Throwable e){
        getLog().error(msg,e);
    }
    
    private ResourceCopyHelper getResourceCopyHelper(){
        return new ResourceCopyHelper(mavenResourcesFiltering, project, session, encoding);
    }
}

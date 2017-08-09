package com.mmiholdings.k8.plugin;

import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;


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

    protected final ProcessBuilderHelper processBuilderHelper = new ProcessBuilderHelper(getLog());
        
    protected void info(String msg){
        getLog().info(msg);
    }

    protected void error(String msg,Throwable e){
        getLog().error(msg,e);
    }
    
    protected static final String MINUS_F = "-f";


}

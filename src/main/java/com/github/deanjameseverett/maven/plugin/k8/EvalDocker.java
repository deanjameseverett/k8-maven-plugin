package com.github.deanjameseverett.maven.plugin.k8;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * I can not get this to work :(
 * Not sure how to do a eval from Java.
 * eval $(minikube docker-env)
 */
@Deprecated
public class EvalDocker {
    private final String tmpDir = System.getProperty(TMP_DIR_PROP);
    private final ProcessBuilderHelper processBuilderHelper;
    
    public EvalDocker(ProcessBuilderHelper processBuilderHelper){
        this.processBuilderHelper = processBuilderHelper;
    }

    public void eval() throws IOException, InterruptedException{
        
        // make a tmp shell script
        File file = new File(tmpDir,SHELL_SCRIPT_FILENAME);
        try (FileWriter fw = new FileWriter(file)) {
            fw.append(BIN_BASH);
            fw.append(NEW_LINE);
            fw.append(EVAL);
        }

        file.setExecutable(true);

        processBuilderHelper.executeCommandWithResult(SH,file.getName());
    }
    
    private static final String SH = "sh";
    private static final String SHELL_SCRIPT_FILENAME = "k8mavenplugin.sh";
    private static final String TMP_DIR_PROP = "java.io.tmpdir";
    private static final String BIN_BASH = "#!/bin/bash";
    private static final String NEW_LINE = "\n";
    private static final String EVAL = "eval $(minikube docker-env)";
}

# Kubernetes Maven Plugin

**Version 1.0.2-SNAPSHOT**

This is a maven plugin that can be installed locally and 
on the central artifactory repo.

The aim of this plugin is to allow developers and automation developers
to build and deploy docker images to kubernetes

---

## Contributors

* [Dean James-Everett](https://github.com/deanjameseverett)
* [Phillip Kruger](https://github.com/phillip-kruger)

## Pre-Requirements

Make sure minikube is running and you have exported the minikube environment

	minikube start	
	eval $(minikube docker-env)

## Installation to Local repo 

    mvn clean install

Otherwise it will be pulled from artefactory when you add it to your maven pom.xml

## Usage

For each maven project add the plugin (you might want to do this in a profile)
Example below will delete and build the docker image. And then redeploy to kubernetes
    
    <build>
        <plugins>
            <plugin>
                <groupId>com.github.deanjameseverett</groupId>
                <artifactId>k8-maven-plugin</artifactId>
                <version>1.0.2-SNAPSHOT</version>
                <executions>
                    <execution>
                        <id>container</id>
                        <phase>install</phase>
                        <goals>
                            <goal>deleteImage</goal>
                            <goal>buildImage</goal>
                            <goal>undeploy</goal>
                            <goal>deploy</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>


Command line Execution

    mvn k8:buildImage
    mvn k8:deleteImage
    mvn k8:deploy
    mvn k8:undeploy
    
    
## How to structure you project

### Docker files.

You can add your Docker file, and any other text based files you want to use in a directory:

    src/main/docker/

The default directory is `src/main/docker/`, however you can define your own:

    <configuration>
        <dockerConfDir>myOwn/dir</dockerConfDir>
    </configuration>

### Kubernetes files

You can add your Kubernetes files in a directory:

    src/main/k8

The default directory is `src/main/k8/`, however you can define your own:

    <configuration>
        <kubernetesConfDir>myOwn/dir</kubernetesConfDir>
    </configuration>
    
The files selection at current is supported add follows

    persistence.yml
    claim.yml
    deployment,yml
    service.yml
    
You can also add config map files under a folder `config` in the Kubernetes directory

    src/main/k8/config
    
## Variables in your configuraion files

All files in the Docker and Kubernetes folders has access to maven variables. 
Example `src/main/docker/Dockerfile`

    FROM airhacks/wildfly
    MAINTAINER Phillip Kruger, phillip-kruger.com
    ENV DEPLOYMENT_DIR ${WILDFLY_HOME}/standalone/deployments/
    RUN rm ${WILDFLY_HOME}/bin/standalone.conf
    ADD standalone.conf ${WILDFLY_HOME}/bin/
    ADD @project.build.finalName@.@project.packaging@ ${DEPLOYMENT_DIR}

@project.build.finalName@ and @project.packaging@ is defined in pom.xml

## Plugin configuration

In most cases you can just use the default values, however if you need to you can set them as plugin configurations:

* target (default to ${project.build.directory})
* encoding (default to ${project.build.sourceEncoding})
* artefactName (default to ${project.build.finalName})
* artefactType (default to ${project.packaging})
* imageName (default to ${project.artifactId})
* imageVersion (default to ${project.version})
* dockerConfDir (default to ${project.basedir}/src/main/docker)
* dockerFileName (default to Dockerfile)
* kubernetesConfDir (default to ${project.basedir}/src/main/k8)
* persistenceFileName (default to persistence.yml)
* claimFileName (default to claim.yml)
* deploymentFileName (default to deployment.yml)
* serviceFileName (default to service.yml)

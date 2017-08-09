# Kubernetes Maven Plugin

**Version 1.0.0**

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

For each maven project add the plugin in the 
    
    <build>
        <plugins>
            <plugin>
                <groupId>com.mmiholdings.k8.plugin</groupId>
                <artifactId>mmik8-maven-plugin</artifactId>
                <version>1.0</version>
                <executions>
                    <execution>
                        <id>mmik8-compile</id>
                        <phase>compile</phase>
                        <goals>
                            <goal>buildImage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>


Command line Execution

    mvn mmik8:buildImage
    mvn mmik8:deleteImage
    mvn mmik8:deploy
    mvn mmik8:undeploy
    
    
How to structure you project

    src/main/docker/Dockerfile
    
The plugin looks for a kubernetes yml files in this structure

    src/main/K8
    
The files selection at current is supported add follows

    persistence.yml
    claim.yml
    deployment,yml
    service.yml
    

    
    


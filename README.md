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
    
The plugin looks for a kubernetes yml files in this structure

    src/main/k8
    
The files selection at current is supported add follows

    persistence.yml
    claim.yml
    deployment,yml
    service.yml
    

    
    


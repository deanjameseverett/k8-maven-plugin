# Kubernetes Maven Plugin

**Version 1.0.5**

This is a maven plugin that can be installed locally and 
on the central artifactory repo.

The aim of this plugin is to allow developers and automation developers
to build and deploy docker images to kubernetes

---

## Contributors

* [Dean James-Everett](https://github.com/deanjameseverett)
* [Phillip Kruger](https://github.com/phillip-kruger)
* [Grant Edwards](grant@symcoiq.com)
* [Michal Hubert Siemaszko](https://ideas.into.software/)

## Pre-Requirements

If not using 'startMinikube' goal directly, make sure minikube is running and you have exported the minikube environment

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
                <version>1.0.4</version>
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
    mvn k8:apply
    mvn k8:startMinikube
    mvn k8:stopMinikube
    
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
    
You can specify any Kubernetes descriptor when using 'apply' goal:

	<configuration>
		<includeFiles>
			<param>infra-messagebroker-statefulset.yaml</param>
			<param>infra-database-statefulset.yaml</param>
		</includeFiles>
	</configuration>									
    
Alternatively, you can specify any of the supported types of Kubernetes descriptors when using 'deploy' and 'undeploy' goals. The specific types which are currently supported are:

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
* dockerRegistry (default to ${project.properties.dockerRegistry})
* insecureRegistry (used with 'startMinikube' goal, ignored if not specified)
* memory (used with 'startMinikube' goal, ignored if not specified)
* cpus (used with 'startMinikube' goal, ignored if not specified)

You can also explicitly define the files that should be included:
(The default is all files in the folder)

    <configuration>
        <includeFiles>
            <param>standalone.conf</param>
            <param>somefile.txt</param>
        </includeFiles>
    </configuration>

(above will only copy the specified files,standalone.conf and somefile.txt)

You can also add to the existing do not filter list (file type that we do not do pom variable replacement, usually non text based files)
(The current default is deb, rpm, tar, gz, tar.gz, zip, war, ear, jar, rar)

    <configuration>
        <noFilterTypes>
            <param>conf</param>
            <param>doc</param>
        </noFilterTypes>
    </configuration>

(above will add all doc and conf files to the ignore list, so no filter on those)

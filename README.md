# SPA-Processes-REST
This project is a process domain implementation which uses the SPA Core to manage projects and processes. It exposes several RESTful services which allow the client to create projects, processes, upload process files (BPMN2), among other operations.

## Docker
To create a docker image of this project, you should run the command below. This command will run the gradle task `buildDocker` which will build the project, create the jar file and build the docker image using the Dockerfile into the folder `docker`.

```Bash
./gradlew buildDocker
```

Once the image is created, to run the docker container, use command below. This command will run the gradle task `runDockerContainer` which run the docker images just created exposing the port 8080.

```Bash
./gradlew runDockerContainer
```

The docker image is available through [docker hub](https://hub.docker.com/r/ines/spa-processes-rest/).

```Bash
docker pull ines/spa-processes-rest
```

## Operations
|          URL                                |  Method  | Description                                                                                                                                                                                                                               |
|:--------------------------------------------|:--------:|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| /projects                                   | `GET`    | This operation returns all the projects available in the database.                                                                                                                                                                        |
| /projects                                   | `POST`   | This operation creates a new project with a `projectLabel` name as a parameter.                                                                                                                                                           |
| /projects/{projectID}                       | `DELETE` | This operation deletes the project `projectID`.                                                                                                                                                                                           |
| /projects/{projectID}/processes             | `GET`    | This operation returns all the processes associated to the project `projectID`.                                                                                                                                                           |
| /projects/{projectID}/processes             | `POST`   | This operation creates a new process with the following parameters: `processLabel` as name, `format` format of the `processFile`. The process file is a multipart file. Finally the process is associated to the project with `projectID`.|
| /projects/{projectID}/processes/{processID} | `GET`    | This operation return the process file of the process `processID`. The file is a BPMN.                                                                                                                                                    |
| /projects/{projectID}/processes/{processID} | `DELETE` | This operation deletes the process `processID` associated to the project `projectID`.                                                                                                                                                     |
| /projects/importformats                     | `GET`    | This operation returns all the supported import formats                                                                                                                                                                                   |
## Technology
This project was implemented using the following:

- [Spring Boot v1.2.6](http://projects.spring.io/spring-boot/)
- [Spring Framework v4.2.1](http://projects.spring.io/spring-framework/)
- [Spring Data Rest v2.4.2](http://projects.spring.io/spring-data-rest/)
- [Guava](https://github.com/google/guava)
- [Rekord](https://github.com/SamirTalwar/Rekord)

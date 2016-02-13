# This container must be able to run a spring-boot application

FROM java:8

MAINTAINER Piero Divasto (pdivasto@gmail.com)

VOLUME /tmp

COPY ./spa-rest-0.0.1-SNAPSHOT.jar spa-rest.jar

RUN bash -c 'touch /spa-rest.jar'

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/spa-rest.jar"]

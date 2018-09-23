# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
MAINTAINER Tsvetan Georgiev<tsvetan.georgiev@gmail.com>

RUN mkdir -p /tmp/crypto

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/cd-java-sdk-1-2-0.0.1.jar

# Add the application's jar to the container
ADD ${JAR_FILE} cd-fabric-client.jar

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/cd-fabric-client.jar"]


FROM openjdk:21-rc-oracle
MAINTAINER devops
WORKDIR /app
COPY target/demo-0.0.1-SNAPSHOT.jar ./application.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","application.jar"]
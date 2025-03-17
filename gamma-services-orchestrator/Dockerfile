FROM        maven:3.8.4-openjdk-17 as build
ARG         MAVEN_OPTS

EXPOSE      8080

COPY        pom.xml .
COPY        src ./src
RUN         mvn package

FROM        openjdk:17-jdk-slim
EXPOSE      8080
EXPOSE      8081
WORKDIR     /usr/src/app
COPY        --from=build /home/maven/target/gamma-orchestrator-*.jar gamma-orchestrator.jar
ENTRYPOINT  ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dsecurerandom.source=file:/dev/./urandom", "-jar", "gamma-orchestrator.jar"]
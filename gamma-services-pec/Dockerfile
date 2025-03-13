FROM        postesviluppo.azurecr.io/caat-images/caat-maven-jdk-17:8 AS build
ARG         MAVEN_OPTS

EXPOSE      8080

COPY        pom.xml .
COPY        src ./src
RUN         mvn package

##### START STATIC CODE ANALYSIS
LABEL sonar=sonar
RUN ls
FROM sonarsource/sonar-scanner-cli AS sonarscanner
ARG projectKey
ARG version
ARG qualityGates
ARG token
ARG server
COPY --from=build ./home/maven ./code
ENV SONAR_HOST_URL="$server"
#RUN sonar-scanner -Dsonar.projectKey=$projectKey -Dsonar.sources=./code -Dsonar.java.binaries=./code/target/classes -Dsonar.qualitygate.wait=$qualityGates -Dsonar.login=$token -Dsonar.projectVersion=$version -Dsonar.exclusions=**src/test/**/*.*
RUN sonar-scanner -Dsonar.projectKey=$projectKey -Dsonar.sources=./code -Dsonar.java.binaries=./code/target/classes -Dsonar.qualitygate.wait=$qualityGates -Dsonar.login=$token -Dsonar.projectVersion=$version -Dsonar.exclusions=**src/**/*.*
##### END STATIC CODE ANALYSIS

# -Djava.security.egd=file:/dev/./urandom
# -Dsecurerandom.source=file:/dev/./urandom

FROM        postesviluppo.azurecr.io/caat-images/caat-ubi-minimal-jre-17:8
EXPOSE      8080
EXPOSE      8081
WORKDIR     /usr/src/app
COPY        --from=build /home/maven/target/anonymous-users-service-*.jar anonymous-users-service.jar
ENTRYPOINT  ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dsecurerandom.source=file:/dev/./urandom", "-jar", "anonymous-users-service.jar"]
# For Docker running locally
FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD_STAGE
COPY ./ ./
RUN mvn clean package -DskipTests
FROM openjdk:8-jre-alpine
COPY --from=MAVEN_BUILD_STAGE /target/recipe-api-0.0.1-SNAPSHOT.jar /recipe-api.jar
CMD ["java", "-jar", "/recipe-api.jar"]

# For Docker in Virtual Machine with Jenkins
# FROM openjdk:8-jre-alpine
# ARG JAR_FILE=target/*.jar
# COPY $JAR_FILE recipe-api.jar
# ENTRYPOINT ["java", "-jar", "/recipe-api.jar"]
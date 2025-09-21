FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test -x validateStructure

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/applications/app-service/build/libs/*.jar auth-service.jar
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1"
CMD ["java", "-jar", "auth-service.jar"]
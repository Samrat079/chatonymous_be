#####################    testing                     ##################################
#FROM eclipse-temurin:21-jdk AS builder
#WORKDIR /app
#
#COPY . .
#
#RUN chmod +x gradlew
#RUN ./gradlew bootJar --no-daemon
#
#FROM eclipse-temurin:21-jre
#WORKDIR /app
#COPY --from=builder /app/build/libs/*.jar app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "app.jar"]

################          production          ################################

FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootjar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
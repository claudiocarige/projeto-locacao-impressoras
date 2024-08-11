FROM maven:3.9.6-amazoncorretto-17-debian AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install -DskipTests


FROM amazoncorretto:17-alpine

COPY --from=build app/target/mspersistence-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

ENV EUREKA_SERVER_HOST=${EUREKA_SERVER_HOST}
ENV DATABASE_URL=${DATABASE_URL}
ENV DB_PASSWORD_POSTGRESQL=${DB_PASSWORD_POSTGRESQL}
ENV DB_USERNAME_POSTGRESQL=${DB_USERNAME_POSTGRESQL}

ENTRYPOINT ["java", "-jar", "app.jar"]
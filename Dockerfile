FROM maven:3.8-openjdk-17 AS build
COPY . /home/app
WORKDIR /home/app
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine
RUN mkdir -p /app/tmp
WORKDIR /app
COPY --from=build /home/app/target/mts-true-tech-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar"]

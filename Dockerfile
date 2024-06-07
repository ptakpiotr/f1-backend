FROM openjdk:21
COPY target/f1-backend-0.0.1-SNAPSHOT.jar f1-backend-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/f1-backend-0.0.1-SNAPSHOT.jar"]
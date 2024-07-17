FROM openjdk:17-slim
COPY ./build/libs/*T.jar app.jar
ENV profiles=prod
ENV serverPort=8080
CMD ["java", "-jar", "-Dspring.profiles.active=${profiles}", "-Duser.timezone=Asia/Seoul", "app.jar"]
EXPOSE ${serverPort}
FROM amazoncorretto:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8003
ENTRYPOINT ["java","-jar","/app.jar"]

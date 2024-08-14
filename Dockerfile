FROM azul/zulu-openjdk:17
CMD ["./gradlew", "clean", "build"]
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} bts.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/bts.jar"]
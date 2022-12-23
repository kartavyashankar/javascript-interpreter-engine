FROM openjdk:17
WORKDIR /javascript_ide
ARG JAR_FILE="./build/libs/javascript-online-ide-0.0.1-SNAPSHOT.jar"
COPY ${JAR_FILE} ./javascript_ide.jar
ENTRYPOINT ["java", "-jar", "javascript_ide.jar"]
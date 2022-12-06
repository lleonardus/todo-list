FROM openjdk
WORKDIR app
COPY target/todo-list-0.0.1-SNAPSHOT.jar app/todo-list.jar
ENTRYPOINT ["java", "-jar", "app/todo-list.jar"]
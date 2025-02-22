FROM eclipse-temurin:21.0.2_13-jdk-alpine
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", \
  "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", \
  "-Dspring.profiles.active=prod", \
  "-jar", \
  "app.jar" \
]

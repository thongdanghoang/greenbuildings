FROM eclipse-temurin:21.0.2_13-jdk-alpine
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", \
  "-Dspring.profiles.active=prod", \
  "-jar", \
  "app.jar" \
]

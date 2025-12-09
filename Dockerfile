FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew

# Clean properly, don't allow UP-TO-DATE caching
RUN ./gradlew clean --no-build-cache && ./gradlew shadowJar --no-daemon

# See what's in /build/libs
RUN echo "==> JARs built:" && ls -lh /app/build/libs/

FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/build/libs/*-all.jar /app/application.jar

ENV PORT=5556
EXPOSE $PORT

CMD ["sh", "-c", "java -jar /app/application.jar"]
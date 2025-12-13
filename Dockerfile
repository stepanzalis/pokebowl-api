FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean --no-build-cache && ./gradlew shadowJar --no-daemon
RUN echo "==> JARs built:" && ls -lh /app/build/libs/

# --- FINAL STAGE ---
FROM amazoncorretto:17
WORKDIR /app

RUN yum -y update && yum -y install wget && yum clean all

COPY --from=build /app/build/libs/*-all.jar /app/application.jar

# That is perfectly fine and good practice.
ENV PORT=5556
EXPOSE $PORT

CMD ["sh", "-c", "java -jar /app/application.jar"]
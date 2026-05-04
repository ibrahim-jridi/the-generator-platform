FROM eclipse-temurin:17-jre-focal
ENV TZ=Africa/Tunis
RUN apt-get update && apt-get install -y curl tzdata && rm -rf /var/lib/apt/lists/*
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

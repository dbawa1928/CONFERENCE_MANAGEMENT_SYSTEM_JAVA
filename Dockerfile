# Build stage with Maven and Eclipse Temurin JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY backend/pom.xml ./backend/
RUN mvn -f backend/pom.xml dependency:go-offline -B || true

# Copy source code and build package
COPY backend/src ./backend/src
RUN mvn -f backend/pom.xml clean package -DskipTests

# Runtime stage with lightweight JRE 17
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/backend/target/confsync-backend-1.0.0.jar app.jar

# Render assigns dynamic PORT environment variable
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar --spring.profiles.active=h2"]

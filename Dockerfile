# ============================================================================
# Dockerfile - Galactic Tournament API
# DBMS: PostgreSQL 16
# Java: 25
# ============================================================================

# Stage 1: Build
FROM maven:3.9.11-eclipse-temurin-25 AS builder

LABEL maintainer="Backend Team"
LABEL description="Multi-stage Docker build for Galactic Tournament API"

# Set working directory
WORKDIR /workspace

# Copy Maven POM file
COPY pom.xml .

# Install dependencies (without needing mvn wrapper)
RUN mvn dependency:go-offline -DskipTests

# Copy source code
COPY src src

# Build application
RUN mvn clean package -DskipTests -q

# ============================================================================
# Stage 2: Runtime
# ============================================================================
FROM eclipse-temurin:25-jre-alpine

LABEL maintainer="Backend Team"
LABEL description="Runtime image for Galactic Tournament API"
LABEL version="1.0"

# Install curl for health checks
RUN apk add --no-cache curl

# Set working directory
WORKDIR /app

# Create non-root user
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

# Copy built application from builder stage
COPY --from=builder /workspace/target/galactic-tournament-api-*.jar app.jar
COPY --from=builder /workspace/target/classes/application.yaml /app/config/application.yaml

# Change ownership to non-root user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f -H "X-API-Key: test-key-123" http://localhost:8080/api/swagger-ui.html || exit 1

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"
ENV TZ=UTC

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]



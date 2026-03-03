FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

COPY pom.xml .
RUN apt-get update -qq && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

RUN curl -s https://get.sdkman.io | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install maven"

COPY . .

RUN mvn clean package -DskipTests --no-transfer-progress

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENV JAVA_OPTS="\
    -XX:InitialRAMPercentage=70.0 \
    -XX:MaxRAMPercentage=70.0 \
    -XX:+UseZGC \
    -XX:+ZGenerational \
    -XX:+AlwaysPreTouch \
    -Djava.awt.headless=true"

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
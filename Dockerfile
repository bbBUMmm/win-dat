FROM maven:3.9.6-eclipse-temurin-21 as builder

WORKDIR /application

COPY pom.xml .

COPY domain domain
COPY outbound-repository-jpa outbound-repository-jpa
COPY inbound-controller-ws inbound-controller-ws
COPY springboot springboot
COPY api-spec api-spec

RUN mvn package
RUN mkdir build && cd build && java -Djarmode=layertools -jar ../springboot/target/*.jar extract

FROM eclipse-temurin:21.0.1_12-jdk-alpine
WORKDIR /application

COPY --from=builder application/build/dependencies/ ./
COPY --from=builder application/build/spring-boot-loader/ ./
COPY --from=builder application/build/snapshot-dependencies/ ./
COPY --from=builder application/build/application/ ./

RUN addgroup -S boot && adduser -S boot -G boot
USER boot

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher"]
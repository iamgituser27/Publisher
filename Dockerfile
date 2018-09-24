FROM openjdk:8
VOLUME /tmp
COPY target/gs-spring-cloud-gcp-publisher-0.1.0.jar /app2.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app2.jar" ]
#FROM openjdk:8-jdk-alpine

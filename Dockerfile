FROM eclipse-temurin:21-jre

# Defaults
ENV DEFAULT_OPTS="-XX:MaxRAMPercentage=95 -XshowSettings:vm"

# RUN apt-get update && \
#     apt-get upgrade -y && \
#     apt-get -y install vim bash curl && \
#     apt-get autoremove -y && \
#     apt-get clean

ARG JAR_FILE=target/*.jar

EXPOSE 8080

RUN groupadd --gid 65532 nonroot
RUN useradd --uid 65532 --gid 65532 -m -d /home/nonroot nonroot

RUN mkdir /app
RUN chown -R nonroot:nonroot /app

USER nonroot:nonroot

WORKDIR /app

COPY ${JAR_FILE} app.jar
CMD ["/bin/sh", "-c", "java ${DEFAULT_OPTS} -jar app.jar"]

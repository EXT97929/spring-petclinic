FROM eclipse-temurin:21-jre

# Defaults
ENV DEFAULT_OPTS="-XX:MaxRAMPercentage=95 -XshowSettings:vm"

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get -y install vim bash curl && \
    apt-get autoremove -y && \
    apt-get clean

ARG APP_NAME
ARG APP_VERSION
ARG JAR_FILE="target/${APP_NAME}-${APP_VERSION}.jar"

EXPOSE 8080

RUN groupadd --gid 65532 nonroot
RUN useradd --uid 65532 --gid 65532 -m -d /home/nonroot nonroot

RUN mkdir /app
RUN mkdir /app/logs
RUN chown -R nonroot:nonroot /app
RUN chown -R nonroot:nonroot /app/logs

USER nonroot:nonroot

WORKDIR /app

RUN mv *.jar app.jar
ADD app.jar app.jar
CMD ["/bin/sh", "-c", "java ${DEFAULT_OPTS} -jar app.jar -XX:+UseShenandoahGC"]
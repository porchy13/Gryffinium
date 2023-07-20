# Pre-build op
FROM debian:bullseye-slim AS unzip

RUN apt-get update \
    && apt-get -y install unzip \
    &&  rm -rf /var/lib/apt/lists/*

WORKDIR /app/

# Binaries
COPY ./gryffinium/target/universal/gryffinium-*.zip .
RUN unzip gryffinium-*.zip \
    && mv gryffinium-*/* . \
    && rm -r gryffinium-* \
    && chmod +x /app/bin/gryffinium

# Startup script
COPY ./docker/gryffinium.sh .
RUN chmod +x gryffinium.sh

# Actual image
FROM eclipse-temurin:17-focal
LABEL maintainer=${GAPSD_MAINTAINER}
LABEL version=${GAPSD_VERSION}
LABEL description=${GAPSD_DESCRIPTION}
WORKDIR /app/

COPY --from=unzip /app/ .

EXPOSE 9000/tcp

ENTRYPOINT ["/app/gryffinium.sh"]

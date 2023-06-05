FROM gradle:jdk17-alpine

RUN apk add --no-cache \
    binutils

COPY . /app
WORKDIR /app

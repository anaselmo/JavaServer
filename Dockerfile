FROM ubuntu:latest

WORKDIR /app

RUN apt-get update && apt-get install -y \
    openjdk-19-jdk-headless \
    sqlite3 \
    iproute2 \
    && rm -rf /var/lib/apt/lists/*

ENV PORT=8080

EXPOSE 8080

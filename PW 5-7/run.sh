#!/bin/bash

rm -f /app/marketplace/build/libs/marketplace.jar

./gradlew build > /dev/null 2>&1

cd ./app/docker

docker-compose up -d


#!/bin/bash

rm -f /build/libs/marketplace.jar

./gradlew build > /dev/null 2>&1

cd ./docker

docker-compose up -d


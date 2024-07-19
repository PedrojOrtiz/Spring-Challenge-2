#!/bin/bash

#mvn package -Dmaven.test.skip=true

docker build -f "./Dockerfile" . -t "challenge2-app"
#
docker tag challenge2-app challenge2-app:dev


#!/usr/bin/env bash

rm -R target

sbt ';clean ;assembly'

docker build -t alekslitvinenk/imdb-app:latest -f Dockerfile.javaApp ./target/scala-2.12
docker push alekslitvinenk/imdb-app:latest

#docker build -t alekslitvinenk/imdb:$NEW_UUID .
#docker push alekslitvinenk/imdb:$NEW_UUID
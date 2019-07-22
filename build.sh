#!/usr/bin/env bash

rm -R target

sbt ';clean ;compile; ;assembly'

docker build -t alekslitvinenk/imdb-app:latest -f Dockerfile.webApp ./target/scala-2.12
docker push alekslitvinenk/imdb-app:latest

docker build -t alekslitvinenk/imdb-db-importer:latest -f Dockerfile.dbImporterApp ./target/scala-2.12
docker push alekslitvinenk/imdb-db-importer:latest
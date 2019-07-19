#!/usr/bin/env bash

docker run --privileged \
-v /var/run/docker.sock:/var/run/docker.sock \
-e DOCKER_USER="alekslitvinenk" \
-e DOCKER_PASSWORD="Ichufef@17" \
-it alekslitvinenk/imdb bash
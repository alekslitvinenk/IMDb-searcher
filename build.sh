#!/usr/bin/env bash

NEW_UUID=latest

docker build -t alekslitvinenk/imdb:$NEW_UUID --no-cache .
#docker push alekslitvinenk/imdb:$NEW_UUID
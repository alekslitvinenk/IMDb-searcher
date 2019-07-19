#!/usr/bin/env bash

DB_ROOT_PASSWORD="jobjob"

docker run --name imdb-mysql \
-e MYSQL_ROOT_PASSWORD="jobjob" \
-p 3306:3306 \
-d mysql


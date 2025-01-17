# 🍿 IMDb-searcher

Simple tool to search information in IMDb datasets. This solution comes with bundled web-app with UI

## Quick Guide

In this guide we assume that the user has at least 8G memory on his/her machine.

1. Run MySQL image in docker:<br>
    ```bash
    docker run \
    --name imdb-mysql \
    --memory="5g" --memory-swap="25g" \
    -e MYSQL_ROOT_PASSWORD=<somepassword> -d -p 3306:3306 mysql
    ```
  
2. Create database named `imdb`

3. Download the data from https://datasets.imdbws.com/<br>
    ```bash
    cd /opt && \
    mkdir data && \
    cd data && \
    wget https://datasets.imdbws.com/name.basics.tsv.gz && \
    gunzip name.basics.tsv.gz && \
    wget https://datasets.imdbws.com/title.basics.tsv.gz && \
    gunzip title.basics.tsv.gz && \
    wget https://datasets.imdbws.com/title.principals.tsv.gz && \
    gunzip title.principals.tsv.gz && \
    wget https://datasets.imdbws.com/title.ratings.tsv.gz && \
    gunzip title.ratings.tsv.gz
    ```
    
4. Build the project:<br>
    ```bash
    ./build.sh
    ```

4. Run DB importer in docker:<br>
    ```bash
    docker run --network="host" \
    --name=importer \
    --memory="1300m" --memory-swap="25g" \
    -v "/opt/data":"/opt/data" \
    --rm \
    -d \
    alekslitvinenk/imdb-db-importer \
    "/opt/data/title.basics.tsv" \
    "/opt/data/title.principals.tsv" \
    "/opt/data/title.ratings.tsv" \
    "/opt/data/name.basics.tsv"
     ```
     
5. Create partitions on tables

6. Run web application in docker:<br>
   ```bash
    docker run \
    -d \
    --memory="500m" --memory-swap="10g" \
    -p 8080:8080 \
    --name=webapp \
    --rm \
    alekslitvinenk/imdb-app 0.0.0.0
    ```

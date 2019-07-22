# 🍿 IMDb-searcher

Searches information in IMDb datasets

## Quick Guide
1. Run MySQL image in docker:<br>
    ```bash
    docker run --name imdb-mysql -e MYSQL_ROOT_PASSWORD=jobjob -d -p 3306:3306 mysql
    ```
  
2. Create database named `imdb`

3. Download the data from https://datasets.imdbws.com/

4. Run DB importer in docker:<br>
    ```bash
    docker run --network="host" \
    -v "/Users/alitvinenko/Downloads":"/opt/data" \
    alekslitvinenk/imdb-db-importer \
    "/opt/data/title_basics.txt" \
    "/opt/data/title_principals.txt" \
    "/opt/data/title_ratings.txt" \
    "/opt/data/name_basics.txt"
     ```
     
5. Create partitions on tables

6. Run web application in docker:<br>
   ```bash
    docker run -p 8080:8080 alekslitvinenk/imdb-app 0.0.0.0
    ```
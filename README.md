# 🍿 IMDb-searcher

Searches information in IMDb datasets

## Quick Guide
1. Run MySQL image in docker:<br>
    ```bash
    docker run --name imdb-mysql -e MYSQL_ROOT_PASSWORD=jobjob -d -p 3306:3306 mysql
    ```
  
2. Create database named `imdb`

3. Run DB importer in docker:<br>
    ```bash
    docker run -it -p 8080:8080 alekslitvinenk/imdb-db-importer
     ```
     
4. Create partitions on tables

5. Run web application in docker:<br>
   ```bash
    docker run -it -p 8080:8080 alekslitvinenk/imdb-app
    ```
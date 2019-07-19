FROM docker:18.09.6

LABEL maintainer="Alexander Litvinenko <array.shift@yahoo.com>"

ENV APP_NAME imdbs
ENV APP_INSTALL_PATH /opt/${APP_NAME}

RUN set -e && \
    mkdir -p ${APP_INSTALL_PATH} && \
    apk add --no-cache netcat-openbsd bash mysql-client && \
    docker run --name imdb-mysql \
    -e MYSQL_ROOT_PASSWORD="jobjob" \
    -p 3306:3306 \
    -d mysql && \
    sleep 20 && \



EXPOSE 3306/tcp

CMD [ "$@" ]
FROM openjdk:11.0-jre
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
ENTRYPOINT exec java -cp IMDb-searcher-assembly-0.1.jar "com.github.alekslitvinenk.PopulateDB"
CMD [ "$@" ]
# minecraft-webapp

## Build and Run Standalone

~~~
$ mvn clean verify
$ export APP_REGISTRATION_DIRECTORY=$(pwd)/data
$ java -jar target/quarkus-app/quarkus-run.jar
~~~

## Build and Run in Docker

~~~
$ mvn clean verify
$ docker build -f src/main/docker/Dockerfile -t minecraft-webapp .
$ docker run -it --rm --name myminecraft \
  -e APP_REGISTRATION_DIRECTORY=/data \
  -v $(pwd)/data:/data \
  -p 8080:8080 \
  minecraft-webapp
~~~

## URLs

~~~
$ curl 'http://localhost:8080/app/rest/registration' -i

$ curl 'http://localhost:8080/app/rest/registration' -i -X POST \
  -H 'content-type: application/json' \
  -d '{"email":"philip.oswald@gmail.com","name":"Philip Oswald","userid":"phoswald","comment":"nix\r\nda"}'
~~~

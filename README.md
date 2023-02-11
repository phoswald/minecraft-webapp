# sample-jquery-mustache-spa

Experiments with jQuery and Mustache (a Poor Man's SPA)

## Build and Run Standalone

~~~
$ mvn clean verify
$ export APP_TASK_DIRECTORY=./data
$ java -jar target/quarkus-app/quarkus-run.jar
~~~

### Dev Mode

~~~
$ mvn quarkus:dev
~~~

## Build and Run in Docker

~~~
$ mvn clean verify
$ docker build -f src/main/docker/Dockerfile -t sample-jquery-mustache-spa .
$ docker run -it --rm --name sample-jquery-mustache-spa \
  -e APP_TASK_DIRECTORY=/data \
  -v ./data:/data \
  -p 8080:8080 \
  sample-jquery-mustache-spa
~~~

## URLs

~~~
$ curl 'http://localhost:8080/app/rest/tasks' -i

$ curl 'http://localhost:8080/app/rest/tasks' -i -X POST \
  -H 'content-type: application/json' \
  -d '{"title":"Blubber..."}'
  
$ curl 'http://localhost:8080/app/rest/tasks/e9788e65-3ceb-4592-8d61-d88d4c8e05f6' -i -X DELETE
~~~

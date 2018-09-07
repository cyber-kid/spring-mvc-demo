#!/bin/sh

docker container rm -fv $(docker ps -qf name=app)
docker rmi spring-mvc-demo
echo y | docker volume prune
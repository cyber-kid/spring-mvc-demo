#!/bin/sh

echo '##################################'
echo '# Building sources with maven... #'
echo '##################################'

docker run \
--rm \
-v $(pwd)/../:/usr/src/mymaven \
-w /usr/src/mymaven \
#-v $HOME/.m2:/root/.m2 \
maven:3-alpine \
mvn clean install

exit_code=$?
if [ "${exit_code}" -gt "0" ]
then
	echo y | docker volume prune > /dev/null
    echo "Error occured. Please, check the output."
    exit 1
fi

echo '############################'
echo '# Creating docker image... #'
echo '############################'

docker build \
-t=spring-mvc-demo \
../

exit_code=$?
if [ "${exit_code}" -gt "0" ]
then
    echo "Error occured. Please, check the output."
    exit 1
fi

echo '#######################'
echo '# Starting MailHog... #'
echo '#######################'

docker run \
-p 8025:8025 \
-p 1025:1025 \
--name app_mailhog \
-d \
mailhog/mailhog 

#exit_code=$?
if [ "${exit_code}" -gt "0" ]
then
    docker rmi spring-mvc-demo > /dev/null
    echo y | docker volume prune > /dev/null
    echo "Error occured. Please, check the output."
    exit 1
fi

echo '#####################'
echo '# Starting MySQL... #'
echo '#####################'

docker run \
-v $(pwd)/../sql/:/sql/ \
--name app_mysql \
-d \
-p 3307:3306 \
-e MYSQL_ROOT_PASSWORD=my-root-pw \
-e MYSQL_DATABASE=app \
-e MYSQL_USER=app \
-e  MYSQL_PASSWORD=test \
mysql

exit_code=$?
if [ "${exit_code}" -gt "0" ]
then
	docker container rm -fv $(docker ps -qf name=app) > /dev/null
	docker rmi spring-mvc-demo > /dev/null
	echo y | docker volume prune > /dev/null
    echo "Error occured. Please, check the output."
    exit 1
fi

echo '######################################'
echo '# Waiting for MySQL to initialize... #'
echo '######################################'

sleep 20

echo '###########################################'
echo '# Deploying DB schema and initial data... #'
echo '###########################################'

docker exec -it app_mysql mysql -v -t -u app --password=test app -e 'source /sql/schema.sql'
docker exec -it app_mysql mysql -v -t -u app --password=test app -e 'source /sql/data.sql'

exit_code=$?
if [ "${exit_code}" -gt "0" ]
then
    docker container rm -fv $(docker ps -qf name=app) > /dev/null
    docker rmi spring-mvc-demo > /dev/null
    echo y | docker volume prune > /dev/null
    echo "Error occured. Please, check the output."
    exit 1
fi

echo '###################################'
echo '# Starting Spring MVC Demo app... #'
echo '###################################'

docker run \
-p 8082:8080 \
-d \
-v $(pwd)/../env/docker:/config/ \
--name app_spring-mvc-demo \
spring-mvc-demo

exit_code=$?
if [ "${exit_code}" -gt "0" ]
then
	docker container rm -fv $(docker ps -qf name=app) > /dev/null
    docker rmi spring-mvc-demo > /dev/null
    echo y | docker volume prune > /dev/null
    echo "Error occured. Please, check the output."
    exit 1
fi
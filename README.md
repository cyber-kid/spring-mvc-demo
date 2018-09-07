# Spring MVC Demo
Simple web page template to practice with Spring MVC and Spring Boot.

## Installation
In this section I will describe how to build and run the project. This section consists of:
  1. Prerequisites;
  2. Cloning the project;
  3. Changing `application.properties`;
  4. Building source code;
  5. Creating a docker image;
  6. Starting MailHog server in a docker container;
  7. Starting MySQL server in a docker container;
  8. Running sql scripts;
  9. Starting Spring MVC Demo app in a docker container;

#### Prerequisites
Yuo should have:
- docker installed and running. Please, see details [for Windows](https://docs.docker.com/docker-for-windows/install/), [for Mac](https://docs.docker.com/docker-for-mac/install/);
- git installed and running. Please, see details [here](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git);
- command line interface;
- text editor.;

All commands were created and tested on Unix machine. Details for Windows machines will be provided as comments.
#### Cloning the project
To clone the project run:
```
https://github.com/cyber-kid/spring-mvc-demo.git
```
When the command exits you will have a folder `spring-mvc-demo` in your working directory. Navigate into this folder. You will see the following elements:
- `env` folder. Contains `dev` and `docker` subfolders with `application.properties` files for different environments.
- `k8s` folder. Contains files to configure Kubernetes cluster with Spring MVC Demo app.
- `scripts` folder. Contains shell scripts to build, deploy, stop, start the Spring MVC Demo app (on Unix).
- `sql` folder. Contains sql scripts to creat db schema and insert initial data into db.
- `src` folder. Contains Java sources.
- `Dockerfile` file. Is Used to create a docker image to run Spring MVC Demo app in a container.
- `pom.xml` file. Is used to build sources with Maven.
#### Changing `application.properties`
Navigate to `./env/docker` folder. Open the `application.properties` file in your favorite text editor. Replace all occurances of `<your_host_ip>` with IP of your network adapter (ex. `192.168.1.240`)(not `127.0.0.1`). Save your changes.
#### Building source code
Navigate back to the root of the project(`spring-mvc-demo` folder). And run the following command:
```
docker run \
--rm \
-v $(pwd)/:/usr/src/mymaven \
-w /usr/src/mymaven \
maven:3-alpine \
mvn clean install
```
This command will do the following:
- start a `maven:3-alpine` container;
- mount a host project root directory (`spring-mvc-demo` folder) to containers `/usr/src/mymaven` folder(`-v $(pwd)/:/usr/src/mymaven`). So the container will have acces to the project folder. On Windows you should replace `$(pwd)/` with `%cd%`;
- will navigate to containers `/usr/src/mymaven` folder (-w /usr/src/mymaven);
- run `mvn clean install` command inside the container to build our Java sources;
- when container will finish building sources, it will destroy itself (`--rm`);

Optionally you can mount you local `.m2` repository to containers `/root/.m2` folder. To do that you can add `-v /path/to/maven/repository/:/root/.m2` after `-v $(pwd)/:/usr/src/mymaven`. This will make the build faster, because Maven container will reuse local artifacts.
#### Creating a docker image
Make sure you are in root of the project(`spring-mvc-demo` folder). And run the following command:
```
docker build -t=spring-mvc-demo .
```
This command will create a docker image named spring-mvc-demo. Do not forget a `. (dot)` in the end, it indicates where docker should look for a `Dockerfile` (`.` means current directory).

If you encounter an error at this stage, you can use `docker images` command to list all existing images or `docker rmi <image_id_or_image_name>` to delete image by ID or name. To recreate an image, you have to delete already existing image with the same name.
#### Starting MailHog server in a docker container
The MailHog server is used to intersept all outgoing messages from the application and store them in it's `Inbox` folder. It is very helpful in development and testing. Please, see more details [here](https://github.com/mailhog/MailHog). To start a MailHog server run the following command:
```
docker run \
-p 8025:8025 \
-p 1025:1025 \
--name app_mailhog \
-d \
mailhog/mailhog 
```
This command will do the following:
- start a `mailhog/mailhog` container with a name `app_mailhog` (`--name app_mailhog`). All container's names that are used for the Spring MVC Demo are prefixed with `app_*` so we can find all containers with one command;
- expose 8025 and 1025 container ports to a host (`-p 8025:8025 -p 1025:1025`);
- detach current terminal form the container (`-d`);

You can open MailHog UI by http://localhost:8025/. Port `1025` is used in `application.properties` as `spring.mail.port=1025`.

If you encounter an error at this stage, you can use `docker ps -a` command to list all containers or `docker rm -f app_mailhog` to stop and delete a container by name. To run a container, you have to delete an already existing one with the same name.
#### Starting MySQL server in a docker container
The MySQL server is used to store user details. To start a MySQL server run the following command from project root directory:
```
docker run \
-v $(pwd)/sql/:/sql/ \
--name app_mysql \
-d \
-p 3307:3306 \
-e MYSQL_ROOT_PASSWORD=my-root-pw \
-e MYSQL_DATABASE=app \
-e MYSQL_USER=app \
-e MYSQL_PASSWORD=test \
mysql
```
This command will do the following:
- start a `mysql` container;
- mount a host directory with sql scripts (`spring-mvc-demo/sql` folder) to containers `/sql` folder(`-v $(pwd)/sql/:/sql/`). So the container will have acces to the sql scripts. On Windows you should replace `$(pwd)/sql/` with `%cd%\sql\`;
- expose 3307 (port 3307 is used not to overlap with local db) container port to a host (`-p 3307:3306`);
- set envirinment variable `MYSQL_ROOT_PASSWORD=my-root-pw`. The value `my-root-pw` will be a `root` user password for db instance;
- set envirinment variable `MYSQL_DATABASE=app`. The value `app` will be a db name that will be created;
- set envirinment variable `MYSQL_USER=app`. The value `MYSQL_USER=app` will be a name of a user that wiil be created in db;
- set envirinment variable `MYSQL_PASSWORD=test`. The value `test` will be a password of `app` user;
- detach current terminal form the container (`-d`);

If you will decide to use different values for `MYSQL_DATABASE`, `MYSQL_USER` or `MYSQL_PASSWORD` you should include them in `application.properties`.

If you encounter an error at this stage, you can use `docker ps -a` command to list all containers or `docker rm -f app_mysql` to stop and delete a container by name. To run a container, you have to delete an already existing one with the same name.
#### Running sql scripts
To create initial db schema and uplod initial data to db run the following commands:
```
docker exec -it app_mysql mysql -v -t -u app --password=test app -e 'source /sql/schema.sql'
docker exec -it app_mysql mysql -v -t -u app --password=test app -e 'source /sql/data.sql'
```
Those commands will do the following:
- connect to `app_mysql` container (`exec -it app_mysql`);
- run `mysql` command to connect to db instance usin credentials provided in previous step. Pass sql scripts to be executed (from the folder that we mounted in previous step)(`mysql -v -t -u app --password=test app -e 'source /sql/schema.sql`);
#### Starting Spring MVC Demo app in a docker container
To start a Spring MVC Demo app run the following command from project root directory:
```
docker run \
-p 8082:8080 \
-d \
-v $(pwd)/env/docker:/config/ \
--name app_spring-mvc-demo \
spring-mvc-demo
```
This command will do the following:
- start a `spring-mvc-demo` container with a name `app_spring-mvc-demo` (`--name app_spring-mvc-demo`);
- expose 8082 container port to a host (`-p 8082:8080`);
- detach current terminal form the container (`-d`);
- mount a host directory with `application.properties` (`spring-mvc-demo/env/docker` folder) to containers `/config` folder(`-v $(pwd)/env/docker:/config/`). The containers `/config` folder is used in `SPRING_CONFIG_LOCATION` environment variable. On Windows you should replace `$(pwd)/env/docker` with `%cd%\env\docker\`;

If you encounter an error at this stage, you can use `docker ps -a` command to list all containers or `docker rm -f app_spring-mvc-demo` to stop and delete a container by name. To run a container, you have to delete an already existing one with the same name.

If all commands are executed successfully you can access Spring MVC Demo app on http://<your_host_ip>:8082;
## Start and Stop
After you successfully builded the app you can stop it using:
```
docker stop app_spring-mvc-demo
docker stop app_mysql
docker stop app_mailhog
```
If you want to start the app again you can use:
```
docker start app_mysql
docker start app_mailhog
docker start app_spring-mvc-demo
```
## Clean up
If you want to delete the app and all related containers you can use:
```
docker container rm -fv $(docker ps -qf name=app)
docker rmi spring-mvc-demo
docker volume prune
```
## Contributing
If you found a bug, have any suggestions or questions feel free to create an issue or pull a request.

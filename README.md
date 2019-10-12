# Spring MVC Demo
Simple web page template to practice with Spring MVC and Spring Boot.

## Installation
In this section I will describe how to build and run the project. This section consists of:
  1. [Prerequisites](#prerequisites);
  2. [Cloning the project](#clone);
  3. [Start and Stop](#start-stop);
  3. [Clean up](#clean-up);

#### <a name="prerequisites" /> Prerequisites
Yuo should have:
- docker installed and running. Please, see details [for Windows](https://docs.docker.com/docker-for-windows/install/), [for Mac](https://docs.docker.com/docker-for-mac/install/);
- git installed and running. Please, see details [here](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git);
- command line interface;

All commands were created and tested on Unix machine. Details for Windows machines will be provided as comments.
#### <a name="clone" /> Cloning the project
To clone the project run:
```
https://github.com/cyber-kid/spring-mvc-demo.git
```
When the command exits you will have a folder `spring-mvc-demo` in your working directory. Navigate into this folder. You will see the following elements:
- `env` folder. Contains `dev` and `docker` subfolders with `application.properties` files for different environments.
- `k8s` folder. Contains files to configure Kubernetes cluster with Spring MVC Demo app.
- `sql` folder. Contains sql scripts to creat db schema and insert initial data into db.
- `src` folder. Contains Java sources.
- `Dockerfile` file. Is Used to create a docker image to run Spring MVC Demo app in a container.
- `docker-compose.yml` file. Is used to start the application and all needed containers.
- `pom.xml` file. Is used to build sources with Maven.

## <a name="start-stop" /> Start and Stop
To start the app navigate to the root folder of the project and run:
```
docker-compose build
```
When the build process is finished run:
```
docker-compose up
```
This command will start 3 containers:
  1. MySQL database container;
  2. MailHog mail server container;
  3. Spring MVC Demo application container;

To stop the application press ```CTRL+C``` in the window where the ```docker-compose up``` was executed.
## <a name="clean-up" /> Clean up
If you want to delete the app and all related containers you can use:
```
docker-compose down
```
## Contributing
If you found a bug, have any suggestions or questions feel free to create an issue or pull a request.

FROM tomcat:8
ENV SPRING_CONFIG_LOCATION /config/
RUN apt-get update && apt-get install -y default-jdk apt-utils
WORKDIR $CATALINA_HOME/webapps/
COPY ./target/spring_mvc_demo.war ROOT.war
RUN rm -r ROOT
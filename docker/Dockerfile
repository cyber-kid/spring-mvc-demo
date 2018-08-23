FROM tomcat:8
#ENV SPRING_CONFIG_LOCATION /config/
RUN apt-get update && apt-get install -y default-jdk apt-utils
COPY . $CATALINA_HOME/webapps/ROOT.war
WORKDIR $CATALINA_HOME/webapps
RUN rm -r ROOT
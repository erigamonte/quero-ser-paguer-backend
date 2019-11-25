FROM tomcat:9.0
COPY target/desafio.war /usr/local/tomcat/webapps/desafio.war
RUN sh -c "touch /usr/local/tomcat/webapps/desafio.war"
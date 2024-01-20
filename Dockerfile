
FROM tomcat:latest

COPY target/web-servlet-service-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/web-app.war

CMD ["catalina.sh", "run"]

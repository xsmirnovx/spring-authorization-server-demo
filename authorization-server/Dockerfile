FROM adoptopenjdk:11-jre-hotspot
ADD target/authorization-server/*.jar application.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /application.jar" ]

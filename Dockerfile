FROM openjdk:17-jdk

WORKDIR /app
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} /app/app.jar
ARG SCOUTER=/scouter/agent.java
COPY ${SCOUTER} /app/scouter/agent.java
ENTRYPOINT ["java","-jar","-javaagent:scouter/agent.java/scouter.agent.jar","-Dscouter.config=scouter/agent.java/scouter.agent.jar/conf/scouter.conf","-Duser.timezone=Asia/Seoul","app.jar"]
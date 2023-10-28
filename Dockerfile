FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dscouter.config=/home/ubuntu/scouter/agent.java/conf/was01.conf","-Duser.timezone=Asia/Seoul","app.jar"]
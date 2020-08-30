FROM openjdk:8-jdk-alpine
MAINTAINER lorbush.com
VOLUME /tmp
EXPOSE 4444
ADD target/account-transactions-report-0.0.1-SNAPSHOT.jar springbootpostgresqldocker.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/springbootpostgresqldocker.jar"]
#ENTRYPOINT ["java", "-Dserver.servlet.context-path=/trxReport", "-jar", "springbootpostgresqldocker.jar"]
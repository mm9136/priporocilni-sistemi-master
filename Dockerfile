FROM adoptopenjdk:8-jre-hotspot

RUN mkdir /app

WORKDIR /app

ADD ./api/target/api-1.0.0-SNAPSHOT.jar /app
#ADD ./api/target/ /app

EXPOSE 8081

ENTRYPOINT ["java", "-jar","api-1.0.0-SNAPSHOT.jar"]

#CMD java -cp classes:dependency/* com.kumuluz.ee.EeApplication


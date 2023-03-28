FROM    maven:3.9.0-amazoncorretto-19

LABEL   author="Chris Wright"

ENV     PORT=8080

WORKDIR /app

COPY    ./api-server/pom.xml ./pom.xml

COPY    ./api-server/src ./src

RUN     mvn clean install

FROM    amazoncorretto:19-alpine-jdk

EXPOSE  $PORT

WORKDIR /app

COPY --from=0 ./app/target ./target

ENTRYPOINT ["java", "-jar", "target/url-shortener-0.0.1-SNAPSHOT.jar"]
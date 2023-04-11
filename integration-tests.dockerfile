FROM    maven:3.9.0-amazoncorretto-19

LABEL   author="Chris Wright"

ENV     PORT=8080

WORKDIR /app

COPY    ./api-server/pom.xml ./pom.xml

COPY    ./api-server/src/ ./src/

RUN     mvn clean package

ENTRYPOINT ["mvn", "verify"]

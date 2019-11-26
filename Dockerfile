FROM openjdk:8-jdk-alpine
COPY ./target/desafio.war /usr/src/desafio/
WORKDIR /usr/src/desafio
EXPOSE 8080
CMD ["java", "-jar", "desafio.war"]
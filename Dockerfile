FROM maven:3-alpine as builder
WORKDIR /source
COPY . .
RUN mvn clean install

FROM openjdk:8-alpine
COPY --from=builder /source/target/spring_mvc_demo.jar ./
CMD java -jar spring_mvc_demo.jar
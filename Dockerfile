FROM openjdk:17
ARG VERSION
COPY build/libs/idea-rush-${VERSION}.jar ./app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java","-jar","./app.jar"]
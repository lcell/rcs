FROM java:8

VOLUME /tmp

ARG JAR_FILE

ADD ${JAR_FILE} app.jar

RUN bash -c 'touch /app.jar'

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar /app.jar  --sping.profiles.active=dev --spring.datasource.username=$username --spring.datasource.url=$url --spring.datasource.password=$password"]

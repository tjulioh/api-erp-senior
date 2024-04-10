FROM amazoncorretto:21-alpine
VOLUME /tmp
COPY api-erp-senior/target/aplicacao.jar aplicacao.jar
ENTRYPOINT ["java", "-jar", "aplicacao.jar"]
FROM navikt/java:8
MAINTAINER audun.persson@nav.no

ARG JAR_FILE
RUN echo ${JAR_FILE}
COPY target/${JAR_FILE} app.jar
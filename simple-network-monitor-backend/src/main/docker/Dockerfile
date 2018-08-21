FROM openjdk:8-jdk-alpine
RUN apk add monitoring-plugins
COPY maven/snm.jar snm.jar

RUN apk add bash && apk add nmap nmap-nselibs nmap-ncat nmap-nping nmap-scripts && \
    apk add bc && apk add curl && \
    curl -L https://github.com/mcktr/check_tr64_fritz/archive/v2.3.tar.gz | tar -xz && mv check_tr64_fritz-2.3 check_tr64_fritz
ENV PATH /usr/lib/monitoring-plugins:/check_tr64_fritz:$PATH
#ENV PATH /usr/lib/monitoring-plugins:$PATH
#ENV NMAP_HOME /usr/

EXPOSE 8080
EXPOSE 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/snm.jar"]
#ENTRYPOINT ["java",  "-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/snm.jar"]

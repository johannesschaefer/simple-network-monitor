FROM openjdk:8-jdk-alpine

RUN apk add monitoring-plugins && \
    apk add bash && \
    apk add nmap nmap-nselibs nmap-ncat nmap-nping nmap-scripts && \
    apk add bc && \
    apk add curl && \
    curl -L https://github.com/mcktr/check_tr64_fritz/archive/v2.3.tar.gz | tar -xz && \
    mv check_tr64_fritz-2.3 check_tr64_fritz

ENV PATH /usr/lib/monitoring-plugins:/check_tr64_fritz:$PATH

ADD target/snm.jar snm.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/snm.jar"]
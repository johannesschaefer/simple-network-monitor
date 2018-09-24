FROM openjdk:8-jdk-alpine

RUN apk add monitoring-plugins && \
    apk add bash && \
    apk add nmap nmap-nselibs nmap-ncat nmap-nping nmap-scripts && \
    apk add bc && \
    apk add curl && \
    apk add samba-common && \
    apk add samba-common-tools && \
    curl -L https://github.com/mcktr/check_tr64_fritz/archive/v2.3.tar.gz | tar -xz && \
    mv check_tr64_fritz-2.3 check_tr64_fritz

ENV ENV="/root/.ashrc"
RUN echo "alias etherwake='busybox ether-wake'" > "$ENV"

ENV PATH /usr/lib/monitoring-plugins:/check_tr64_fritz:$PATH

COPY maven/snm.jar snm.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/snm.jar"]
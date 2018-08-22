FROM resin/rpi-raspbian:latest

RUN apt-get update && apt-get -qy install oracle-java8-jdk curl ca-certificates monitoring-plugins nmap && \
    curl -L https://github.com/mcktr/check_tr64_fritz/archive/v2.3.tar.gz | tar -xz && \
    mv check_tr64_fritz-2.3 check_tr64_fritz



#  nmap-nselibs nmap-ncat nmap-nping nmap-scripts

ENV PATH /usr/lib/nagios/plugins:/check_tr64_fritz:$PATH

RUN curl -L https://github.com/johannesschaefer/simple-network-monitor/releases/download/v0.0.1-alpha/snm.jar --output snm.jar

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/snm.jar", "--libPath=/usr/lib/monitoring-plugins"]
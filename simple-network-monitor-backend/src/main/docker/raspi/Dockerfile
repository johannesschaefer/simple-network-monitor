FROM resin/rpi-raspbian:latest

RUN apt-get update
RUN apt-get -qy install etherwake net-tools oracle-java8-jdk curl ca-certificates monitoring-plugins nmap samba-common-bin && \
    curl -L https://github.com/mcktr/check_tr64_fritz/archive/v2.3.tar.gz | tar -xz && \
    mv check_tr64_fritz-2.3 check_tr64_fritz



#  nmap-nselibs nmap-ncat nmap-nping nmap-scripts

ENV PATH /usr/lib/nagios/plugins:/check_tr64_fritz:$PATH

COPY maven/snm.jar snm.jar

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/snm.jar", "--libPath=/usr/lib/monitoring-plugins"]
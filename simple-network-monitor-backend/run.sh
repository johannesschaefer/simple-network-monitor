#/bin/bash
docker stop snm || true
docker rm snm || true
mvn clean package -DskipTests &&
cp src/main/docker/local/Dockerfile target &&
docker build -t snm target &&
docker run -d -p 8080:8080 -p 5005:5005 --name snm snm

sleep 2
#/bin/bash
docker stop snm || true
docker rm snm || true
mvn clean package -DskipTests -P-with-frontend &&
cp src/main/docker/local/Dockerfile target &&
docker build -t snm target &&
docker run -d -p 8080:8080 -p 5005:5005 -e "LOG_LEVEL_INTERN=TRACE" -e "DEFAULT_NETWORK=192.168.178.0/24" -e "UNSECURE_EXPORT=true" --name snm snm

sleep 1
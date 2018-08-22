# Simple Network Monitor

*still under development* please wait for the first release before you report bugs or missing features.

[![Build Status](https://travis-ci.org/johannesschaefer/simple-network-monitor.svg?branch=master)](https://travis-ci.org/johannesschaefer/simple-network-monitor)

The Simple Network Monitor (SNM) is a small service to monitor other hosts or network devices. It checks cyclic the availability of certain services. The SNM saves all probes in a database and creates charts out of it.

## How to run

In all cases the application will be available under http://localhost:8080/, it is possible to access the application from outside by replacing localhost with your hostname or IP address.

### Run the Java file directly

Download the Jar file from the Github release page and run the Jar with with following command.

```
java -jar snm.jar
```

Requirements:
* Java 8
* Monitor Plugins must be available in the PATH
* nmap must be available in the PATH

### Docker

Install docker on your system.

Then just run the following command:

```
docker build -t snm https://github.com/johannesschaefer/simple-network-monitor.git#:simple-network-monitor-backend/src/main/docker/remote && docker run -dit --name snm -p 8080:8080 snm
```

### Docker on Raspberry

Please install docker. A good description can be found here: https://blog.alexellis.io/getting-started-with-docker-on-raspberry-pi/

Then just run the following command:

```
docker build -t snm https://raw.githubusercontent.com/johannesschaefer/simple-network-monitor/master/simple-network-monitor-backend/src/main/docker/remote-raspi/Dockerfile && docker run -dit --name snm -p 8080:8080 snm
```

On a RaspberryPi 1 the startup can take several minutes, but it runs.

### General Docker hints

To change the public port, just change the first `8080` to the port number you want.

# Simple Network Monitor

**still under heavy development** please wait for the first release before you report bugs or missing features.

[![Build Status](https://travis-ci.org/johannesschaefer/simple-network-monitor.svg?branch=master)](https://travis-ci.org/johannesschaefer/simple-network-monitor)
![Docker Automated build](https://img.shields.io/docker/automated/johannesschafer/simple-network-monitor.svg)
![Docker Build Status](https://img.shields.io/docker/build/johannesschafer/simple-network-monitor.svg)
![GitHub tag](https://img.shields.io/github/tag/johannesschaefer/simple-network-monitor.svg)

The Simple Network Monitor (SNM) is a small service to monitor other hosts or network devices. It checks cyclic the availability of certain services. The SNM saves all probes in a database and creates charts out of it.

## How to run

In all cases the application will be available under http://localhost:8080/, it is possible to access the application from outside by replacing localhost with your hostname or IP address.

### Run the Java file directly

Download the Jar file from the Github [release page](https://github.com/johannesschaefer/simple-network-monitor/releases) and run the Jar with with following command.

```
java -jar snm.jar
```

Requirements:
* Java 8
* [Monitor Plugins](https://www.monitoring-plugins.org) must be available in the PATH
* [nmap](https://nmap.org) must be available in the PATH

### Docker

Install docker on your system.

Then just run the following command:

```
docker run -dit --name simple-network-monitor -p 8080:8080 johannesschafer/simple-network-monitor
```

Run with an persisted H2 db:

```
docker run -dit --name simple-network-monitor -p 8080:8080 -e "DB_URL=jdbc:h2:/db/snmdb" johannesschafer/simple-network-monitor
```

Please check the simple-network-monitor-backend/src/main/docker-compose directory for docker-compose examples. Here you can also find the setup for different databases.

### Docker on Raspberry

Install docker on your system. A good description can be found here: https://blog.alexellis.io/getting-started-with-docker-on-raspberry-pi/

Then just run the following command:

```
docker build -t snm https://raw.githubusercontent.com/johannesschaefer/simple-network-monitor/master/simple-network-monitor-backend/src/main/docker/remote-raspi/Dockerfile && docker run -dit --name snm -p 8080:8080 snm
```

On a RaspberryPi 1 the startup can take several minutes, but it runs.

### General Docker hints

To change the public port, just change the first `8080` to the port number you want.


## Setting

The following settings can be set externally. By default all settings have useful values for a first test run. For a productive usage these settings should be adjusted.

| Name            | Description                                           | Commandline                                       | Docker                          | Default           |
|-----------------|-------------------------------------------------------|---------------------------------------------------|---------------------------------|-------------------|
| H2 Console      | Enables the H2 console                                | --spring.h2.console.enabled=...                   | H2_CONSOLE_ENABLED=...          | false             |
| H2 allow others | Expose the H2 console to other clients than localhost | --spring.h2.console.settings.web-allow-others=... | H2_CONSOLE_WEB_ALLOW_OTHERS=... | false             |
| DB URL          | Database URL                                          | --spring.datasource.url=...                       | DB_URL=...                      | jdbc:h2:mem:snmdb |
| DB Username     | Database username                                     | --spring.datasource.username=...                  | DB_USERNAME=...                 | sa                |
| DB Password     | Database password                                     | --spring.datasource.password=...                  | DB_PASSWORD=...                 | org.h2.Driver     |
| DB Driver       | Database driver class name                            | --spring.datasource.driver-class-name=...         | DB_DRIVER=...                   |                   |
| hosts file      | The name of the host file to load on startup          | --hosts-file=...                                  | HOSTS_FILE=...                  | hosts.json        |
| commands file   | The name of the command file to load on startup       | --commands-file=...                               | COMMANDS_FILE=...               | commands.json     |
| setting file    | The name of the setting file to load on startup       | --settings-file=...                               | SETTINGS_FILE=...               | settings.json     |
| Default Network | This network will be shown in the auto discovery dialog | --defaultNetwork=...                              | DEFAULT_NETWORK=...             | 192.168.178.0/24  |
| Unsecure Export | Set this value to true, to export also the passwords  | --unsecureExport=...                              | UNSECURE_EXPORT=...             | false             |
| Hibernate Dialect | | --spring.jpa.properties.hibernate.dialect=... | DB_DIALECT=... | |

For the file parameters, you can use an export of settings, commands or hosts from the application. The refer such a file set the value like `--hosts-file=file:/myHosts.json`.

### Using an persiting database

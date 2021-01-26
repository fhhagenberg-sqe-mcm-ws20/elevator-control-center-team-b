# Elevator Control Center of Team B 
![Build](https://github.com/fhhagenberg-sqe-mcm-ws20/elevator-control-center-team-b/workflows/Build/badge.svg) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fhhagenberg-sqe-mcm-ws20_elevator-control-center-team-b&metric=alert_status)](https://sonarcloud.io/dashboard?id=fhhagenberg-sqe-mcm-ws20_elevator-control-center-team-b) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fhhagenberg-sqe-mcm-ws20_elevator-control-center-team-b&metric=coverage)](https://sonarcloud.io/dashboard?id=fhhagenberg-sqe-mcm-ws20_elevator-control-center-team-b)

Team Members:
- Andreas Roither
- Maike Rieger
- Clemens Tögel

### Prerequisites

- [x] Java 13 SDK (e.g. Oracle or OpenJDK).
- [x] Maven 3. (If you use an IDE like Eclipse or IntelliJ, Maven is **already included** :sunglasses:.)
	- see http://maven.apache.org/install.html
- [x] An IDE or code editor of your choice.

> Confirm your installation with `mvn -v` in a new shell. The result should be similar to:

```
$ mvn -v
Apache Maven 3.6.2 (40f52333136460af0dc0d7232c0dc0bcf0d9e117; 2019-08-27T17:06:16+02:00)
Maven home: C:\Users\winterer\.tools\apache-maven-3.6.2
Java version: 13, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-13
Default locale: en_GB, platform encoding: Cp1252
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

## Instructions

> Run the latests pre-built `.jar` file [latest release](https://github.com/fhhagenberg-sqe-mcm-ws20/elevator-control-center-team-b/releases/latest) with `java -jar FILENAME.jar`

### Run .jar with Maven
- Build application with `mvn clean package`

### Run application with Maven
- Run application with `mvn clean javafx:run`

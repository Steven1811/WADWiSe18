# AddressViz

Based on [spring-boot-vuejs](https://github.com/jonashackt/spring-boot-vuejs)

## Required Software

- [Node.JS 10.14.2 LTS](https://nodejs.org/en/)
- [Maven](https://maven.apache.org/download.cgi)
- [Java JDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html)

## Commands to run for setup
This command needs to be run in the root folder where the frontend and backend folders are located
```bash
mvn clean install
```

## How to start the Backend Spring Boot Server
```bash
mvn --projects backend spring-boot:run
```
The backend is still a work in progress.


## Commands for the Frontend Server
cd into the frontend directory first
```bash
cd frontend
```

### Starting a Hot-Reload Development Server

```bash
npm run serve
```

### Building a production ready build only

```
npm run build
```

The Web Application Archive ready for deployment on Tomcat will be available at `./frontend/target/dist/advi.war`

### Deploy a production ready build to Tomcat

Uses the user `admin` and password `1234`, if you need to change the credentials, look into `package.json` and change the credentials in this line accordingly:
```bash
"buildDeploy": "vue-cli-service build && tomcat-deploy redeploy ./target/dist/advi.war --hostname localhost --port 8080 --login admin --password 1234 --path /advi"
```


```bash
npm run buildDeploy
```

The Single-Page-Application will the be available at http://localhost:8080/advi
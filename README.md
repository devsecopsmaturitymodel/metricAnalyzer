# Metric Analyzer

## Local Installation and Deployment

### Build and Package with Maven

```bash
mvn clean install
mvn compile
mvn package
```

### Add to Docker 
```bash
docker build -t <registry-name>/<docker-name>:<tag> .
docker push <registry-name>/<docker-name>:<tag>
```
# Metric Analyzer

## Local Installation and Deployment

### Build and execute with Maven

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--metricCA.configuration.yaml-path=/home/XXX/git/metricAnalyzer/definitions/configuration.yaml,metricCA.application.yaml-path=/home/XXX/git/metricAnalyzer/definitions/App1.yaml"
```

### Add to Docker 
```bash
docker build -t <registry-name>/<docker-name>:<tag> .
docker push <registry-name>/<docker-name>:<tag>
```
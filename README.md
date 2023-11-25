# Metric Analyzer

## Local Installation and Deployment

### Build and execute with Maven

```bash
mvn clean install
mvn compile
mvn compile; mvn exec:java  -Dexec.mainClass="org.owasp.dsomm.metricCA.analyzer.App" -Dexec.args='"--configuration-yaml-path=/home/XXX/git/metricAnalyzer/definitions/configuration.yaml" "--application-yaml-path=/home/XXX/git/metricAnalyzer/definitions/App1.yaml"'
```

### Add to Docker 
```bash
docker build -t <registry-name>/<docker-name>:<tag> .
docker push <registry-name>/<docker-name>:<tag>
```
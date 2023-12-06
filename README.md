# Metric Analyzer

## Local Installation and Deployment
### Build and execute with Maven

```bash
export spring_profiles_active=dev
mvn spring-boot:run
```

### Add to Docker 
```bash
docker build -t <registry-name>/<docker-name>:<tag> .
docker push <registry-name>/<docker-name>:<tag>
```
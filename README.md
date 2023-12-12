# Metric Analyzer

## Customization for your own metrics
### Skeleton
The file `src/main/resources/sekelton.yaml` contains the metric definitions.

#### Format
The format is as follows:
```yaml 
  conduction of simple threat modeling on a technical level:
    components:
      - string: title
        dateperiod: conduction date
        links:
          - string: title
          - string: url
```
In `components` you find `key` and `value`. In yaml, key is allowed only once. Therefore, please use an array to define values of the same type (see links).

The value can be a string, a dateperiod or a list of links. The dateperiod is a string with the format `yyyy-MM-dd`.

### Content


## Local Installation and Deployment

### Build and execute with Maven

```bash
export spring_profiles_active=dev
mvn spring-boot:run
```


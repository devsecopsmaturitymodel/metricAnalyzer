# Metric Analyzer

## Customization for your own metrics
### Skeleton
The file `src/main/resources/sekelton.yaml` contains the metric definitions.

#### Format
The format is as follows:
```yaml 
TODO
```
In `components` you find `key` and `value`. In yaml, key is allowed only once. Therefore, please use an array to define values of the same type.

The value can be a string, a dateperiod or a list of links. The dateperiod is a string with the format `yyyy-MM-dd`.
### Content


Remark: A team YAML is not required. But if there is a team YAML, an application YAML is required. It is not allowed to have the same activity in a team YAML and an application YAML.
## Local Installation and Deployment

### Build and execute with Maven

```bash
export spring_profiles_active=dev
mvn spring-boot:run
```

## Dashboards
### Panel Types
In case an activity with different periods for different levels is used, we couldn't find a way to display the data in a single panel. Therefore, we created multiple panels for each level.
In a panel, we show the end date of a period, this is not possible in case of multiple periods. It might be possible to have different selectors to switch between Levels in a single panel.

In case an activity with same period for different levels is used, the data can be displayed in a single panel.

### Create new dashboards
Assuming you are using infinity plugin:
- Create panel in grafana and click on three dots and then `Inspect` -> `Panel JSON`
- Put JSON of team and overview panels in `src/main/resources/templates/panel-infinity-overview/<name>.ftl` and/or `src/main/resources/templates/panel-infinity-team/<name>.ftl`
- Exchange the `title` and `URL` in the JSON with `#{title}` and `#{url}` respectively

### Adjust activities

# Development
## Deserialization process (see `ApplicationDirector`)
1. Deserialize the YAML in `resources/skeleton.yaml` in the method  to Java object skeletons (`getDeserializedApplications()` creates `sekelton/SeketlonActivity`)
    1. Using the models in `deserialization/skeleton/Period` to represent the activity details (e.g. `Thresholds` -> `DatePeriod`)
2. Called via `getDeserializedApplications(skeletonActivities)`, the `ActivityDirector` creates the activities from the skeletons and the activity YAML files from git (or for development local file system `/definitions`)
    1. The `ActivityFactory` is called by the `ActivityDirector` to create the activities
    2. The `ActivityFactory` deserializes the YAML files to Java objects
    3. `ActivityFactory` uses the `ActivityBuilder` to transport the data from the skeleton to the activity
3. On request, the `ThresholdDatePeriodManager` calculates the `DatePeriod` based on the given dates form YAMLs and threshold-`Period`s
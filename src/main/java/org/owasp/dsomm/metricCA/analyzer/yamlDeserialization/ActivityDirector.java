package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.owasp.dsomm.metricCA.analyzer.exception.ComponentNotFoundException;
import org.owasp.dsomm.metricCA.analyzer.exception.SkeletonNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ActivityDirector {
  private static final Logger logger = LoggerFactory.getLogger(ActivityDirector.class);

  private final Map<String, Activity> activities;
  private ArrayList<String> nester;

  public ActivityDirector() {
    this.activities = new HashMap<>();
    this.nester = new ArrayList<String>();
  }

  // (1) Creates activities with its components
  public void createActivities(Map<?, ?> javaYaml) throws SkeletonNotFoundException, ComponentNotFoundException {
    Map<?, ?> activityDefinition = (Map<?, ?>) javaYaml.get("activity definitions");
    for (Map.Entry<?, ?> entry : activityDefinition.entrySet()) {
      String key = (String) entry.getKey();
      LinkedHashMap<?, ?> value = (LinkedHashMap<?, ?>) entry.getValue();

      createActivity(key, value);
    }
  }

  // (1) Helper function uses ActivityBuilder
  private void createActivity(String activityName, LinkedHashMap<?, ?> data) throws SkeletonNotFoundException, ComponentNotFoundException {
    // Initializes a new Activity Builder, creating a corresponding Activity along with an empty ArrayList for its components which will be added to Activity when builder builds component.
    ActivityBuilder builder = new ActivityBuilder();

    // Get Level
    String level = (String) data.get("level");

    builder = builder
        .setActivityName(activityName)
        .setLevel(level);

    // Add Components in the builder
    ArrayList arr = (ArrayList) data.get("components");
    addComponents(builder, arr);

    Activity activity = builder.build();

    // Add Activity to the HashMap
    activities.put(activityName, activity);
  }

  // (1) Helper function for create Activity
  private void addComponents(ActivityBuilder builder, ArrayList data) throws SkeletonNotFoundException, ComponentNotFoundException {
    for (int j = 0; j < data.size(); j++) {
      logger.debug("data.get(j)" + data.get(j));

      LinkedHashMap components = (LinkedHashMap) data.get(j);
      List<Object> keyList = new ArrayList<>(components.keySet());

      for (int i = 0; i < keyList.size(); i++) {
        Object key = keyList.get(i);
        Object value = components.get(key);

        // TODO Change!!!
        if (value instanceof String) {
          String normalizedValue = key.toString().replaceAll("-.*", "");
          switch (normalizedValue) {
            case "string":
              logger.info(value.toString());
              builder.addStringComponent(value.toString(), nester);
              break;
            case "date":
              builder.addDateComponent(value.toString(), nester);
              break;
            case "dateperiod":
              String periodLength = key.toString().replaceAll(".*-", "");
              builder.addDatePeriodComponent(value.toString(), periodLength, nester);
              break;
            case "int":
              builder.addIntComponent(value.toString(), nester);
              break;
            default:
              throw new ComponentNotFoundException("Component '" + key + "' doesn't exists");
          }
        } else if (value instanceof ArrayList && nester.isEmpty()) {
          nester.add(key.toString());
          ArrayList<Object> arr = (ArrayList<Object>) value;
          addComponents(builder, arr);
          nester = new ArrayList<String>();
        } else if (value instanceof ArrayList && !nester.isEmpty()) {
          nester.add(key.toString());
          ArrayList<Object> arr = (ArrayList<Object>) value;
          addComponents(builder, arr);
        } else {
          throw new SkeletonNotFoundException("This instance does not exist! value: " + value);
        }
        //System.out.println("Key: " + key + ", Value: " + value);
      }
    }
  }

  public Map<String, Activity> getActivities() {
    return activities;
  }
}

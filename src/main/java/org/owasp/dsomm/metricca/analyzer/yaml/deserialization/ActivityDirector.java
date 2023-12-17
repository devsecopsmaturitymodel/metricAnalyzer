package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.owasp.dsomm.metricca.analyzer.exception.ComponentNotFoundException;
import org.owasp.dsomm.metricca.analyzer.exception.SkeletonNotFoundException;
import org.owasp.dsomm.metricca.analyzer.model.Activity;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Threshold;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Thresholds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Array;
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
  public void createActivities(Map<?, ?> javaYaml) throws SkeletonNotFoundException, ComponentNotFoundException, IOException {
    Map<?, ?> activityDefinition = (Map<?, ?>) javaYaml.get("activity definitions");
    for (Map.Entry<?, ?> entry : activityDefinition.entrySet()) {
      String key = (String) entry.getKey();
      LinkedHashMap<?, ?> value = (LinkedHashMap<?, ?>) entry.getValue();

      createActivity(key, value);
    }
  }

  // (1) Helper function uses ActivityBuilder
  private void createActivity(String activityName, LinkedHashMap<?, ?> data) throws SkeletonNotFoundException, ComponentNotFoundException, IOException {
    String thresholdsName = "thresholds";
    String thresoldsString = writeObjectAsString(thresholdsName, data);
    Thresholds thresholds = ThresholdParser.parseYaml(thresoldsString);

    for (Threshold threshold : thresholds.getThresholds()) {
      logger.info("threshold: " + threshold);
      // Initializes a new Activity Builder, creating a corresponding Activity along with an empty ArrayList for its components which will be added to Activity when builder builds component.
      ActivityBuilder builder = new ActivityBuilder();

      builder = builder
          .setActivityName(activityName)
          .setLevel(threshold.getLevel());

      // Add Components in the builder
      ArrayList arr = (ArrayList) data.get("components");
      addSkeletons(builder, arr, threshold);

      Activity activity = builder.build();
      activity.setThresholds(thresholds);


      // Add Activity to the HashMap
      activities.put(activityName + " " + threshold.getLevel(), activity);
    }
    logger.info("activities created: " + activities);
  }

  private String writeObjectAsString(String nodeName, LinkedHashMap<?, ?> data) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String tresholdString = mapper.writeValueAsString(data.get(nodeName));
    String fullString = "{\"" + nodeName + "\":" + tresholdString + "}";

    return fullString;
  }

  // (1) Helper function for create Activity
  private void addSkeletons(ActivityBuilder builder, ArrayList data, Threshold threshold) throws SkeletonNotFoundException, ComponentNotFoundException {
    for (int j = 0; j < data.size(); j++) {
      logger.debug("data.get(j)" + data.get(j));

      LinkedHashMap components = (LinkedHashMap) data.get(j);
      List<Object> keyList = new ArrayList<>(components.keySet());

      for (int i = 0; i < keyList.size(); i++) {
        Object key = keyList.get(i);
        Object value = components.get(key);

        // TODO Change!!!
        if (value instanceof String) {
          switch (key.toString()) {
            case "string":
              logger.info(value.toString());
              builder.addStringComponent(value.toString(), nester);
              break;
            case "date":
              builder.addDateComponent(value.toString(), nester);
              break;
            case "dateperiod":
              String periodLength = threshold.getDatePeriod().getPeriod();
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
          addSkeletons(builder, arr, threshold);
          nester = new ArrayList<String>();
        } else if (value instanceof ArrayList && !nester.isEmpty()) {
          nester.add(key.toString());
          ArrayList<Object> arr = (ArrayList<Object>) value;
          addSkeletons(builder, arr, threshold);
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
  public Collection<Activity> getActivities(String name) {
    ArrayList<Activity> activitiesToReturn = new ArrayList<>();
    for(Activity activity : activities.values()) {
      if(activity.getNameWithLevel().equals(name)) {
        activitiesToReturn.add(activity);
      }
    }
    return activitiesToReturn;
  }
}

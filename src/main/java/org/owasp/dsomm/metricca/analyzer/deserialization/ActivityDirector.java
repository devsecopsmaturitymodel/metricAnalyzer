package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.SkeletonActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ActivityDirector {
  private static final Logger logger = LoggerFactory.getLogger(ActivityDirector.class);

  private final List<Activity> activities;


  public ActivityDirector(JsonNode activityObjects, List<SkeletonActivity> skeletonActivities) throws JsonProcessingException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    List<Activity> activities = new ArrayList<>();
    for (SkeletonActivity skeletonActivity : skeletonActivities) {
      for (String activityName : skeletonActivity.getActivityNames()) {
        Class<? extends org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity> clazz = (Class<? extends org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity>) Class.forName("org.owasp.dsomm.metricca.analyzer.deserialization.activity." + skeletonActivity.getClassName());
        JsonNode activityData = activityObjects.get(activityName);
        SkeletonActivity newSkeletonActivity = copyObject(skeletonActivity);
        org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity activity = ActivityFactory.createActivity(activityName, activityData, clazz, newSkeletonActivity);
        activities.add(activity);
      }
    }
    this.activities = activities;
  }

  private SkeletonActivity copyObject(Object objSource) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    SkeletonActivity deepCopy = objectMapper
        .readValue(objectMapper.writeValueAsString(objSource), SkeletonActivity.class);
    return deepCopy;
  }

  public List<Activity> getActivities() {
    return activities;
  }
}

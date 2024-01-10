package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.databind.JsonNode;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.exception.SkeletonNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);
  private final List<org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity> activities;
  private String team;
  private String application;
  private String desiredLevel;

  public Application(JsonNode applicationYamlReader, List<SkeletonActivity> skeletonActivities, String kind) throws SkeletonNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    ActivityDirector activityDirector = new ActivityDirector(applicationYamlReader, skeletonActivities, kind);
    this.activities = activityDirector.getActivities();
  }

  public List<Activity> getActivities() {
    return activities;
  }

  public List<Activity> getActivities(String activityName) {
    List<Activity> activitiesToReturn = new ArrayList<Activity>();
    for (Activity activity : getActivities()) {
      if (activity.getName().equals(activityName)) {
        activitiesToReturn.add(activity);
      }
    }
    return activitiesToReturn;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public String getApplication() {
    return application;
  }

  public void setApplication(String application) {
    this.application = application;
  }

  public String getDesiredLevel() {
    return desiredLevel;
  }

  public void setDesiredLevel(String desiredLevel) {
    this.desiredLevel = desiredLevel;
  }
}

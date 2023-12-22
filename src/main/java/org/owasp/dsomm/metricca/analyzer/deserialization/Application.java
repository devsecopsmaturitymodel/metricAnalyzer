package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.databind.JsonNode;
import org.owasp.dsomm.metricca.analyzer.exception.SkeletonNotFoundException;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class Application {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);
  private final List<org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity> activities;
  private String team;
  private String application;
  private String desiredLevel;
  private String mapKeyForCompare;

  public Application(JsonNode applicationYamlReader, List<SkeletonActivity> skeletonActivities) throws SkeletonNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    ActivityDirector activityDirector = new ActivityDirector(applicationYamlReader, skeletonActivities);
    this.activities = activityDirector.getActivities();
  }

  public List<Activity> getActivities() {
    return activities;
  }

  public List<Activity> getActivity(String activityName) {
    List<Activity> activitiesToReturn = new ArrayList<Activity>();
    for (Activity activity : getActivities()) {
      if (activity.getName().equals(activityName)) {
        activitiesToReturn.add(activity);
      }
    }
    return activitiesToReturn;
  }

  public List<Activity> getActivities(String activityName, String level) {
    List<Activity> activitiesToReturn = new ArrayList<Activity>();
    for (Activity activity : getActivity(activityName)) {
        activitiesToReturn.add(activity); // TODO Level
    }
    return activitiesToReturn;
  }

  private String getMapKeyForCompare(ArrayList<Map<String, Object>> content) {
    for (Map<String, Object> componentMap : content) {
      for (String key : componentMap.keySet()) {
        if (key.contains("date")) {
          return key;
        }
      }
    }
    return "";
  }

  private void sortContent(Activity activity) { // TODO
//    this.mapKeyForCompare = getMapKeyForCompare(activity.getContent());
//    Comparator<Map<String, Object>> dateComparator = (map1, map2) -> {
//      Date date1 = (Date) ((Component) map1.get(this.mapKeyForCompare)).getValue();
//      Date date2 = (Date) ((Component) map2.get(this.mapKeyForCompare)).getValue();
//      return date1.compareTo(date2);
//    };
//    Collections.sort(activity.getContent(), dateComparator);
  }
  public List<Date> getDatesFromActivities() {
    List<Date> datesToReturn = new ArrayList<Date>();
    for(Activity activity : getActivities()) {
      for(org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date date : activity.getDateComponents()) {
        datesToReturn.add(date.getDate());
      }
    }
    return datesToReturn;
  }
  private List<Activity> enrichComponentsOfActivities() {
//    Iterator<Activity> iterator = getActivities().iterator();
//    while (iterator.hasNext()) {
//      Activity activity = iterator.next();
//      if (hasDateComponent(activity)) {
//        this.sortContent(activity);
//      }
//
//      boolean containsDate = false;
//      ArrayList<Map<String, Object>> newContent = new ArrayList<Map<String, Object>>();
//      for (int i = 0; i < activity.getContent().size(); i++) {
//        Map<String, Object> componentMap = activity.getContent().get(i);
//        for (String key : componentMap.keySet()) {
//          if (key.contains("date")) {
//            containsDate = true;
//            break;
//          }
//        }
//        if (containsDate) {
//          for (Object component : componentMap.values()) {
//            if (component instanceof DatePeriodComponent && !(component instanceof DatePeriodEndComponent)) {
//              DatePeriodEndComponent end = new DatePeriodEndComponent(((DatePeriodComponent) component).getPeriod());
//              end.setName(((DatePeriodComponent) component).getName());
//              end.setValue(((DatePeriodComponent) component).getValue());
//              HashMap<String, Object> content = new HashMap<String, Object>();
//              content.put(((DatePeriodComponent) component).getName(), end);
//              if (isPeriodBetweenTwoDates(activity, i, end.getValue())) {
//                logger.debug("Found date which is between two dates in period, not adding: " + activity.getName() + " in application: " + getApplication() + " with datePeriodComponent: " + component + " team: " + getTeam() + " and value: " + ((DatePeriodComponent) component).isActive());
//                continue;
//              }
//              newContent.add(content);
//            }
//          }
//        }
//      }
//      activity.getContent().addAll(newContent);
//    }
    return getActivities();
  }

  private boolean isPeriodBetweenTwoDates(Activity activity, int currentContentIndex, Date endOfPeriod) {
    return true; // TODO
//    try {
//      activity.getContent().get(currentContentIndex + 1);
//    } catch (java.lang.IndexOutOfBoundsException e) {
//      logger.debug("No next date component found" + e);
//      return false;
//    }
//
//    Date dateAfterStart = null;
//    Map<String, Object> componentMap = activity.getContent().get(currentContentIndex + 1);
//    for (Object component : componentMap.values()) {
//      if (component instanceof DatePeriodComponent && !(component instanceof DatePeriodEndComponent)) {
//        dateAfterStart = ((DatePeriodComponent) component).getValue();
//        break;
//      }
//    }
//
//    logger.debug("dateAfterStart " + dateAfterStart + " endOfPeriod " + endOfPeriod);
//    if (dateAfterStart == null) {
//      return false;
//    }
//    return dateAfterStart.before(endOfPeriod);
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

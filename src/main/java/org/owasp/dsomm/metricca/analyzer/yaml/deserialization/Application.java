package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import org.owasp.dsomm.metricca.analyzer.exception.SkeletonNotFoundException;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DatePeriodComponent;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DatePeriodEndComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

public class Application {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  private String team;
  private String application;
  private final ActivityDirector activityDirector;

  private String desiredLevel;
  private String mapKeyForCompare;

  public Application(Map<?, ?> configJavaYaml) throws SkeletonNotFoundException {
    activityDirector = new ActivityDirector();
    activityDirector.createActivities(configJavaYaml); // (1) Creates skeleton in activities. Each application has one ActivityDirector
  }

  // (2) Main function of application. It clones all components and fill it with content
  public void saveData(Map<?, ?> appJavaYaml) {
    HashMap allActivities = (HashMap) (appJavaYaml.get("activities"));
    for (Object activityKey : allActivities.keySet()) {
      ArrayList data = (ArrayList) allActivities.get(activityKey);
      for (int i = 0; i < data.size(); i++) {
        activityDirector.getActivities().get(activityKey).cloneSkeletonAndAddToContent();
      }
      fillActivityContent(data, activityKey);
    }
    enrichComponentsOfActivities();
  }

  // (2) Helper function of saveData
  private void fillActivityContent(ArrayList data, Object activity) {
    for (int i = 0; i < data.size(); i++) { // Data in each activity
      HashMap<String, Object> tempData = (HashMap<String, Object>) data.get(i);
      Set<Entry<String, Object>> entrySet = tempData.entrySet();
      HashMap comp = tempData;
      Iterator<Object> iterator = comp.keySet().iterator();
      Iterator<Object> valueIterator = comp.values().iterator();
      for (Entry<String, Object> entry : entrySet) {
        Object mayComp = entry.getValue();
        if (mayComp.getClass() == ArrayList.class) {
          ArrayList<?> arrayList = (ArrayList<?>) mayComp;
          HashMap f = (HashMap) arrayList.get(0);
          Set<Entry<String, Object>> enSet2 = f.entrySet();
          for (Entry<String, Object> entry2 : enSet2) {
            HashMap innerMap = (HashMap) activityDirector.getActivities().get(activity).getContent().get(i).get(entry.getKey());
            Component compon = (Component) (innerMap.get(entry2.getKey()));
            compon.setValue(entry2.getValue());
          }
        } else {
          Component editComp = (Component) activityDirector.getActivities().get(activity).getContent().get(i).get(iterator.next());
          Object value = valueIterator.next();
          editComp.setValue(value);
        }
      }
    }
  }

  public Collection<Activity> getActivities() {
    Collection<Activity> activities = activityDirector.getActivities().values();
    return activities;
  }

  public Collection<Activity> getActivities(String activityName) {
    Collection<Activity> activities = getActivities();
    Collection<Activity> activitiesToReturn = new ArrayList<Activity>();
    for (Activity activity : activities) {
      if (activity.getName().equals(activityName)) {
        activitiesToReturn.add(activity);
      }
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

  private void sortContent(Activity activity) {
    this.mapKeyForCompare = getMapKeyForCompare(activity.getContent());
    Comparator<Map<String, Object>> dateComparator = (map1, map2) -> {
      Date date1 = (Date) ((Component) map1.get(this.mapKeyForCompare)).getValue();
      Date date2 = (Date) ((Component) map2.get(this.mapKeyForCompare)).getValue();
      return date1.compareTo(date2);
    };
    Collections.sort(activity.getContent(), dateComparator);
  }

  private Collection<Activity> enrichComponentsOfActivities() {
    Iterator<Activity> iterator = getActivities().iterator();
    while (iterator.hasNext()) {
      Activity activity = iterator.next();
      if (hasDateComponent(activity)) {
        this.sortContent(activity);
      }

      boolean containsDate = false;
      ArrayList<Map<String, Object>> newContent = new ArrayList<Map<String, Object>>();
      for (int i = 0; i < activity.getContent().size(); i++) {
        Map<String, Object> componentMap = activity.getContent().get(i);
        for (String key : componentMap.keySet()) {
          if (key.contains("date")) {
            containsDate = true;
            break;
          }
        }
        if (containsDate) {
          for (Object component : componentMap.values()) {
            if (component instanceof DatePeriodComponent && !(component instanceof DatePeriodEndComponent)) {
              DatePeriodEndComponent end = new DatePeriodEndComponent(((DatePeriodComponent) component).getPeriod());
              end.setName(((DatePeriodComponent) component).getName());
              end.setValue(((DatePeriodComponent) component).getValue());
              HashMap<String, Object> content = new HashMap<String, Object>();
              content.put(((DatePeriodComponent) component).getName(), end);
              if (isPeriodBetweenTwoDates(activity, i, end.getValue())) {
                logger.info("#####Found date which is between two dates in period, not adding: " + activity.getName() + " in application: " + getApplication() + " with datePeriodComponent: " + component + " team: " + getTeam() + " and value: " + ((DatePeriodComponent) component).isActive());
                continue;
              }
              newContent.add(content);
            }
          }
        }
      }
      activity.getContent().addAll(newContent);
    }
    return getActivities();
  }

  private boolean hasDateComponent(Activity activity) {
    for (Map<String, Object> componentMap : activity.getContent()) {
      for (String key : componentMap.keySet()) {
        if (key.contains("date")) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isPeriodBetweenTwoDates(Activity activity, int currentContentIndex, Date endOfPeriod) {
    try {
      activity.getContent().get(currentContentIndex + 1);
    } catch (java.lang.IndexOutOfBoundsException e) {
      logger.debug("No next date component found" + e);
      return false;
    }

    Date dateAfterStart = null;
    Map<String, Object> componentMap = activity.getContent().get(currentContentIndex + 1);
    for (Object component : componentMap.values()) {
      if (component instanceof DatePeriodComponent && !(component instanceof DatePeriodEndComponent)) {
        dateAfterStart = ((DatePeriodComponent) component).getValue();
        break;
      }
    }

    logger.debug("dateAfterStart " + dateAfterStart + " endOfPeriod " + endOfPeriod);
    if (dateAfterStart == null) {
      return false;
    }
    return dateAfterStart.before(endOfPeriod);
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

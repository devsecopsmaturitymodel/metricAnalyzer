package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.exception.ComponentNotFoundException;
import org.owasp.dsomm.metricca.analyzer.exception.SkeletonNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

// Utilizes Singleton Design Pattern
@Component
public class ApplicationDirector {
  private static final Logger logger = LoggerFactory.getLogger(ApplicationDirector.class);

  private static List<Application> applications = new ArrayList<>();
  @Autowired
  private YamlScanner yamlScanner;

  // Direct instantiation not possible
  private ApplicationDirector() {
  }

  public List<Application> getApplications() throws SkeletonNotFoundException, ComponentNotFoundException, IOException, GitAPIException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    yamlScanner.enforceGitCloneIfTargetFolderExists = true; // set in cronjob
    initiateApplications();
    yamlScanner.enforceGitCloneIfTargetFolderExists = false;
    return applications;
  }

  // TODO: CronJob
  private void initiateApplications() throws SkeletonNotFoundException, ComponentNotFoundException, IOException, GitAPIException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    List<SkeletonActivity> skeletonActivities = getDeserializeSkeletons();
    List<Application> applications = getDeserializedApplications(skeletonActivities);
    ApplicationDirector.applications = applications;
  }

  private List<SkeletonActivity> getDeserializeSkeletons() throws IOException, GitAPIException {
    logger.info("yamlConfigurationFilePath: " + yamlScanner.getSkeletonYaml());

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    Map<?, ?> yamlActivityFileMap = YamlReader.convertYamlToJavaYaml(yamlScanner.getSkeletonYaml().getPath());
    String skeletonString = mapper.writeValueAsString(yamlActivityFileMap.get("activity definitions"));
    logger.info("skeletonString: " + skeletonString);
    List<SkeletonActivity> skeletonActivities = mapper.readValue(skeletonString, new TypeReference<List<SkeletonActivity>>() {
    });
    return skeletonActivities;
  }
  private List<Application> getDeserializedApplications(List<SkeletonActivity> skeletonActivities) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, GitAPIException {
    List<Application> applications = new ArrayList<>();
    YamlApplicationNodes yamlApplicationNodes = new YamlApplicationNodes();
    for (File yamlApplicationFilePath : yamlScanner.getApplicationYamls()) {
      logger.info("yamlApplicationFilePath: " + yamlApplicationFilePath.getPath());
      ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
      JsonNode applicationJsonNode = objectMapper.readTree(new File(yamlApplicationFilePath.getPath()));
      yamlApplicationNodes.addJsonNode(applicationJsonNode);
    }
    for (String teamName : yamlApplicationNodes.getNodes().keySet()) {
      ArrayList<Application> teamApplications = new ArrayList<>();
      for (JsonNode applicationJsonNode : yamlApplicationNodes.getNodes(teamName, "team")) {
        Application application = createApplication(applicationJsonNode, skeletonActivities);
        teamApplications.add(application);
      }
      for (JsonNode applicationJsonNode : yamlApplicationNodes.getNodes(teamName, "application")) {
        Application application = createApplication(applicationJsonNode, skeletonActivities);
        for (Application teamApplication : teamApplications) {
          application.getActivities().addAll(teamApplication.getActivities());
        }
        applications.add(application);
      }
    }
    return applications;
  }

  private Application createApplication(JsonNode jsonNode, List<SkeletonActivity> skeletonActivities) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    JsonNode settings = jsonNode.get("settings");
    Application newApp = new Application(jsonNode.get("activities"), skeletonActivities);
    if (settings.has("application")) {
      newApp.setApplication(settings.get("application").asText());
    }
    if (settings.has("team")) {
      newApp.setTeam(settings.get("team").asText());
    }
    if (settings.has("desired level")) {
      newApp.setDesiredLevel(settings.get("desired level").asText());
    }
    return newApp;
  }

  public LinkedHashMap<String, java.util.Date> getStartAndEndDateFromActivitiesAsMap(String activityName) throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    LinkedHashMap<String, java.util.Date> datesToReturn = new LinkedHashMap<String, java.util.Date>();
    for (String level : getLevelsForActivity(activityName)) {
      for (java.util.Date date : getStartAndEndDateFromActivities(activityName, level)) {
        datesToReturn.put(level, date);
      }
    }

    return sortLinkedHashMap(datesToReturn);
  }

  private List<java.util.Date> getStartAndEndDateFromActivities(String activityName) throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    List<java.util.Date> datesToReturn = new ArrayList<java.util.Date>();
    for (String level : getLevelsForActivity(activityName)) {
      for (java.util.Date date : getStartAndEndDateFromActivities(activityName, level)) {
        datesToReturn.add(date);
      }
    }
    return datesToReturn;
  }

  private LinkedHashMap<String, java.util.Date> sortLinkedHashMap(LinkedHashMap<String, java.util.Date> data) {
    List<Map.Entry<String, java.util.Date>> list = new ArrayList<>(data.entrySet());
    list.sort(Map.Entry.comparingByValue());

    LinkedHashMap<String, java.util.Date> sortedMap = new LinkedHashMap<>();
    for (Map.Entry<String, java.util.Date> entry : list) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }
    return sortedMap;
  }

  private List<String> getLevelsForActivity(String activityName) throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    if (getApplications().size() == 0) {
      return Collections.emptyList();
    }
    for (Activity activity : this.getActivities(activityName)) {
      return activity.getThresholdDatePeriodMap().keySet().stream().toList();
    }
    return Collections.emptyList();
  }

  public List<java.util.Date> getStartAndEndDateFromActivities(String activityName, String level) throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    List<java.util.Date> datesToReturn = new ArrayList<java.util.Date>();
    for (Activity activity : this.getActivities(activityName)) {
      for (java.util.Date date : activity.getThresholdDatePeriodMap().get(level).getStartAndEndDate()) {
        if (date != null) {
          datesToReturn.add(date);
        }
      }
    }
    Comparator<java.util.Date> dateComparator = (date1, date2) -> {
      return date1.compareTo(date2);
    };
    Collections.sort(datesToReturn, dateComparator);
    return datesToReturn;
  }


  private List<Activity> getActivities(String activityName) throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    List<Activity> activitiesToReturn = new ArrayList<Activity>();
    for (Application application : this.getApplications()) {
      for (Activity activity : application.getActivities(activityName)) {
        if (activity.getName().equals(activityName)) {
          activitiesToReturn.add(activity);
        }
      }
    }
    return activitiesToReturn;
  }

  public Collection<FlattenDate> getActivitiesPerTeamFlat(String teamName, String activityName) throws Exception {
    return getActivitiesPerTeamAndApplicationFlat(null, teamName, activityName, null);
  }

  public Collection<FlattenDate> getActivitiesPerTeamAndApplicationFlat(String applicationName, String teamName, String activityName) throws Exception {
    return getActivitiesPerTeamAndApplicationFlat(applicationName, teamName, activityName, null);
  }

  public Collection<FlattenDate> getActivitiesPerTeamAndApplicationFlat(String applicationName, String teamName, String activityName, String level) throws Exception {
    Collection<FlattenDate> flattenedActivitiesToReturn = new ArrayList<FlattenDate>();
    List<java.util.Date> datesFromActivities;
    if (level == null) {
      datesFromActivities = getStartAndEndDateFromActivities(activityName);
      logger.debug("datesFromActivities: " + datesFromActivities);
    } else {
      datesFromActivities = getStartAndEndDateFromActivities(activityName, level);
    }

    logger.info("datesFromActivities: " + datesFromActivities);
    for (java.util.Date date : datesFromActivities) {
      FlattenDate flattenDate = new FlattenDate(date);
      for (Application application : getApplications()) {
        if (applicationName != null && !application.getApplication().equals(applicationName)) {
          continue;
        }
        if (teamName != null && !application.getTeam().equals(teamName)) {
          continue;
        }
        for (Activity activity : application.getActivities(activityName)) {
          boolean value = false;
          org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod dateComponent = null;
          if (activity.getThresholdDatePeriodMap().get(level) == null) {
            logger.debug("1activity.getThresholdDatePeriodMap().get(level) == null");
          } else {
            if (isDateStartDatePeriod(date, activity, level)) {
              logger.info("isDateAStartDatePeriod " + activityName + " " + level + " " + date + " " + application.getTeam());
              value = true;
            } else if (isDateEndDatePeriod(date, activity, level)) {
              logger.info("isDateAEndDatePeriod " + activityName + " " + level + " " + date);
              dateComponent = activity.getThresholdDatePeriodMap().get(level).getDatePeriodEndForDate(date);
              value = !dateComponent.getShowEndDate();
            } else if (isDateEndDatePeriodEnforced(date, activity, level)) {
              logger.info("isDateAEndDateEnforcedPeriod " + activityName + " " + level + " " + date + " " + application.getTeam());
              dateComponent = activity.getThresholdDatePeriodMap().get(level).getClosestBeforeDatePeriodComponent(date);
              if (dateComponent == null) { //no DatePeriod found, that means it is not implemented (yet)
                value = false;
              } else {
                value = dateComponent.isInPeriod(date);
              }
            } else { // it is a DatePeriod from an other activity
              logger.info("an other activity " + activityName + " " + level + " " + date + " " + application.getTeam());
              dateComponent = activity.getThresholdDatePeriodMap().get(level).getClosestBeforeDatePeriodComponent(date);
              if (dateComponent == null) {
                value = false; //  no DatePeriod found, that means it is not implemented (yet)
              } else {
                value = dateComponent.isInPeriod(date);
              }
            }
          }
          flattenDate.addDynamicField(application.getTeam() + "-" + application.getApplication(), value);
        }
      }
      flattenedActivitiesToReturn.add(flattenDate);
    }
    return flattenedActivitiesToReturn;
  }

  private boolean isDateStartDatePeriod(java.util.Date date, Activity activity, String level) {
    DatePeriod dateComponent = activity.getThresholdDatePeriodMap().get(level).getDatePeriodForDate(date);
    return dateComponent != null;
  }

  private boolean isDateEndDatePeriod(java.util.Date date, Activity activity, String level) {
    DatePeriod dateComponent = activity.getThresholdDatePeriodMap().get(level).getDatePeriodEndForDate(date);
    return dateComponent != null;
  }

  private boolean isDateEndDatePeriodEnforced(java.util.Date date, Activity activity, String level) {
    DatePeriod dateComponent = activity.getThresholdDatePeriodMap().get(level).getDatePeriodEndForDateEnforced(date);
    return dateComponent != null;
  }

  public Collection<FlattenDate> getActivitiesFlat(String activityName) throws Exception {
    return getActivitiesPerTeamFlat(null, activityName);
  }

  public LinkedHashMap<String, Collection<FlattenDate>> getActivitiesPerTeamAndApplicationFlatAsLevelMap(String applicationName, String teamName, String activityName) throws Exception {
    LinkedHashMap<String, Collection<FlattenDate>> flattenedActivitiesToReturn = new LinkedHashMap<String, Collection<FlattenDate>>();
    for (String level : getLevelsForActivity(activityName)) {
      flattenedActivitiesToReturn.put(level, getActivitiesPerTeamAndApplicationFlat(applicationName, teamName, activityName, level));
    }
    return flattenedActivitiesToReturn;
  }
}

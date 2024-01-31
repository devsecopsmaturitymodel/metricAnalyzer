package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.exception.ComponentNotFoundException;
import org.owasp.dsomm.metricca.analyzer.exception.SkeletonNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

// Utilizes Singleton Design Pattern
@Component
@EnableScheduling
public class ApplicationDirector {
  private static final Logger logger = LoggerFactory.getLogger(ApplicationDirector.class);

  private static List<Application> applications = new ArrayList<>();

  private static List<SkeletonActivity> skeletonActivities = new ArrayList<>();
  @Autowired
  private YamlScanner yamlScanner;

  // Direct instantiation not possible
  private ApplicationDirector() {
  }

  public static List<SkeletonActivity> getSkeletonActivities() {
    if (skeletonActivities == null) {
      logger.error("skeletonActivities is null, please run initiateApplications() first!");
    }
    return skeletonActivities;
  }

  @Scheduled(fixedRate = 10000)
  public void initiateApplicationsViaCron() throws SkeletonNotFoundException, ComponentNotFoundException, IOException, GitAPIException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    logger.debug("initiateApplicationsViaCron");
    yamlScanner.initiateEnforced();
    initiateApplications();
  }


  public List<Application> getApplications() throws SkeletonNotFoundException, ComponentNotFoundException, IOException, GitAPIException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    if (applications.size() == 0) {
      initiateApplications();
    }
    return applications;
  }

  private void initiateApplications() throws SkeletonNotFoundException, ComponentNotFoundException, IOException, GitAPIException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    skeletonActivities = getDeserializeSkeletons();
    List<Application> applications = getDeserializedApplications(skeletonActivities);
    ApplicationDirector.applications = applications;
  }

  private List<SkeletonActivity> getDeserializeSkeletons() throws IOException, GitAPIException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    yamlScanner.initiate();
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
    HashMap<String, List<Activity>> teamActivities = new HashMap<>();
    for (File yamlApplicationFilePath : yamlScanner.getApplicationYamls()) {
      logger.info("yamlApplicationFilePath: " + yamlApplicationFilePath.getPath());
      ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
      JsonNode applicationJsonNode = objectMapper.readTree(new File(yamlApplicationFilePath.getPath()));
      yamlApplicationNodes.addJsonNode(applicationJsonNode);
    }
    for (String teamName : yamlApplicationNodes.getNodes().keySet()) {
      ArrayList<Application> teamApplications = new ArrayList<>();
      for (JsonNode applicationJsonNode : yamlApplicationNodes.getNodes(teamName, "team")) {
        Application application = createApplication(applicationJsonNode, skeletonActivities, "team");
        teamApplications.add(application);
      }
      for (JsonNode applicationJsonNode : yamlApplicationNodes.getNodes(teamName, "application")) {
        Application application = createApplication(applicationJsonNode, skeletonActivities, "application");
        for (Application teamApplication : teamApplications) {
          application.getActivities().addAll(teamApplication.getActivities());
        }
        applications.add(application);
      }
      for (Application teamApplication : teamApplications) {
        if (teamActivities.containsKey(teamName)) {
          teamActivities.get(teamName).addAll(teamApplication.getActivities());
          continue;
        }
        if (teamActivities.containsKey(teamName)) {
          teamActivities.get(teamName).addAll(teamApplication.getActivities());
        } else {
          teamActivities.put(teamName, teamApplication.getActivities());
        }
      }
    }
    setApplicationFromTeamsYaml(teamActivities);

    return applications;
  }

  private void setApplicationFromTeamsYaml(HashMap<String, List<Activity>> teamActivities) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    List<Team> teams = yamlScanner.getTeamsAndApplicationYaml();
    for (Team team : teams) {
      logger.debug("team: " + team.getName());
      String settings = "activities:" + System.lineSeparator() + "settings:" + System.lineSeparator() + "  team: " + team.getName();
      JsonNode settingsTeamNode = new ObjectMapper(new YAMLFactory()).readTree(settings);
      Application applicationTeam = createApplication(settingsTeamNode, skeletonActivities, "team");
      for (String applicationName : team.getApplications()) {
        if (isApplicationInList(applications, applicationName)) {
          continue;
        }
        String settingsActivity = settings + System.lineSeparator() + "  application: " + applicationName;
        JsonNode settingsActivityNode = new ObjectMapper(new YAMLFactory()).readTree(settingsActivity);
        Application application = createApplication(settingsActivityNode, skeletonActivities, "application");

        if (teamActivities.containsKey(team.getName())) {
          application.getActivities().addAll(teamActivities.get(team.getName()));
        } else {
          application.getActivities().addAll(applicationTeam.getActivities());
        }
        for(Activity activity : application.getActivities()) {
          logger.debug("activity: " + activity.getName() + " " + activity.getKind() + " ");
        }
        applications.add(application);
      }
    }
  }

  private boolean isNameInList(List<Activity> activities, String name) {
    boolean isInList = false;
    for (Activity activity : activities) {
      if (activity.getName().equals(name)) {
        isInList = true;
        break;
      }
    }
    return isInList;
  }

  private Application createApplication(JsonNode jsonNode, List<SkeletonActivity> skeletonActivities, String kind) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    JsonNode settings = jsonNode.get("settings");
    Application newApp = new Application(jsonNode.get("activities"), skeletonActivities, kind);
    if (settings.has("application")) {
      newApp.setName(settings.get("application").asText());
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

  public List<java.util.Date> getStartDateFromActivitiesAsMap(String activityName) throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    List<java.util.Date> datesToReturn = new ArrayList<java.util.Date>();
    for (Application application : getApplications()) {
      for (Activity activity : application.getActivities(activityName)) {
        for (String level : activity.getThresholdDatePeriodMap().keySet()) {
          for (DatePeriod datePeriod : activity.getThresholdDatePeriodMap().get(level).getThresholdDatePeriods()) {
            if (datePeriod.getDate() != null) {
              datesToReturn.add(datePeriod.getDate());
            }
          }
        }
      }
    }

    return datesToReturn;
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
        if (applicationName != null && !application.getName().equals(applicationName)) {
          continue;
        }
        if (teamName != null && !application.getTeam().equals(teamName)) {
          continue;
        }
        for (Activity activity : application.getActivities(activityName)) {
          boolean value = isDateActive(activity, date, level, activityName, application.getTeam());
          String label = getLabel(application.getTeam(), application.getName(), activity, level);
          flattenDate.addDynamicField(label, value);
        }
      }
      flattenedActivitiesToReturn.add(flattenDate);
    }
    return flattenedActivitiesToReturn;
  }

  private boolean isDateActive(Activity activity, Date date, String level, String activityName, String teamName) {
    boolean value = false;
    org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod dateComponent = null;

    if (activity.getThresholdDatePeriodMap() == null || activity.getThresholdDatePeriodMap().get(level) == null) {
      logger.debug("activity.getThresholdDatePeriodMap().get(level) == null");
    } else {
      if (isDateStartDatePeriod(date, activity, level)) {
        logger.info("isDateAStartDatePeriod " + activity.getName() + " " + level + " " + date + " " + teamName);
        value = true;
      } else if (isDateEndDatePeriod(date, activity, level)) {
        logger.info("isDateAEndDatePeriod " + activity.getName() + " " + level + " " + date);
        dateComponent = activity.getThresholdDatePeriodMap().get(level).getDatePeriodEndForDate(date);
        value = !dateComponent.getShowEndDate();
      } else if (isDateEndDatePeriodEnforced(date, activity, level)) {
        logger.info("isDateAEndDateEnforcedPeriod " + activity.getName() + " " + level + " " + date + " " + teamName);
        dateComponent = activity.getThresholdDatePeriodMap().get(level).getClosestBeforeDatePeriodComponent(date);
        if (dateComponent == null) { //no DatePeriod found, that means it is not implemented (yet)
          value = false;
        } else {
          value = dateComponent.isInPeriod(date);
        }
      } else { // it is a DatePeriod from an other activity
        logger.debug("an other activity " + activity.getName() + " " + level + " " + date + " " + teamName);
        dateComponent = activity.getThresholdDatePeriodMap().get(level).getClosestBeforeDatePeriodComponent(date);
        if (dateComponent == null) {
          value = false; //  no DatePeriod found, that means it is not implemented (yet)
        } else {
          value = dateComponent.isInPeriod(date);
        }
      }
    }
    return value;
  }

  private String getLabel(String team, String applicationName, Activity activity, String level) {
    switch (activity.getKind()) {
      case "team":
        return team + " " + level;
      case "application":
        return team + " - " + applicationName + " " + level;
      default:
        return "";
    }
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

  public Collection<FlattenDate> getActivitiesPerTeamAndApplicationFlatAsLevelMap(String applicationName, String teamName, String activityName) throws Exception {
    Collection<FlattenDate> flattenedActivitiesToReturn = new ArrayList<FlattenDate>();

    for (String level : getLevelsForActivity(activityName)) {
      Collection<FlattenDate> flattenedActivities = getActivitiesPerTeamAndApplicationFlat(applicationName, teamName, activityName, level);
      flattenedActivitiesToReturn = mergeFlattenDates(flattenedActivitiesToReturn, flattenedActivities, getActivity(activityName, teamName), level, teamName, applicationName);
    }

    return flattenedActivitiesToReturn;
  }

  private Collection<FlattenDate> mergeFlattenDates(Collection<FlattenDate> flattenDates, Collection<FlattenDate> flattenDatesNew, Activity activity, String level, String teamName, String applicationName) throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    List<FlattenDate> mergedFlattenDates = new ArrayList<>();

    for (FlattenDate flattenDateNew : flattenDatesNew) {
      FlattenDate flattenDateFoundInList = null;
      for (FlattenDate flattenDate : flattenDates) {
        if (flattenDate.getDate().equals(flattenDateNew.getDate())) {
          flattenDateFoundInList = flattenDate;
          break;
        }
      }
      if (flattenDateFoundInList == null) {
        logger.info("flattenDateFoundInList == null" + flattenDateNew.getDate());
        boolean isDateActiveForLevel = isDateActive(activity, flattenDateNew.getDate(), level, activity.getName(), teamName);
        flattenDateNew.addDynamicField(getLabel(teamName, applicationName, activity, level), isDateActiveForLevel);
        for (String otherLevel : getLevelsForActivity(activity.getName())) {
          if (!otherLevel.equals(level)) {
            String label = getLabel(teamName, applicationName, activity, otherLevel);
            if (flattenDateNew.getEntries() != null && flattenDateNew.getEntries().containsKey(label)) {
              logger.info("flattenDateNew.getDynamicFields().containsKey(label) " + label);
              continue;
            }
            boolean isDateActiveForOtherLevel = isDateActive(activity, flattenDateNew.getDate(), otherLevel, activity.getName(), teamName);
            flattenDateNew.addDynamicField(label, isDateActiveForOtherLevel);
          }
        }
      } else {
        logger.info("flattenDateFoundInList != null" + flattenDateFoundInList.getDate());
        flattenDateFoundInList.getEntries().forEach((key, value) -> {
          flattenDateNew.addDynamicField(key, value);
        });
      }

      mergedFlattenDates.add(flattenDateNew);
    }
    return mergedFlattenDates;
  }

  private Activity getActivity(String activityName, String teamName) throws Exception {
    for (Application application : getApplications()) {
      if (application.getTeam().equals(teamName)) {
        for (Activity activity : application.getActivities(activityName)) {
          if (activity.getName().equals(activityName)) {
            return activity;
          }
        }
      }
    }
    throw new Exception("Activity not found");
  }

  private boolean isApplicationInList(List<Application> applications, String name) {
    boolean isInList = false;
    for (Application application : applications) {
      if (application.getName().equals(name)) {
        isInList = true;
        break;
      }
    }
    return isInList;
  }

//  public LinkedHashMap<String, Collection<FlattenDate>> getActivitiesPerTeamAndApplicationFlatAsLevelMap(String applicationName, String teamName, String activityName) throws Exception {
//    LinkedHashMap<String, Collection<FlattenDate>> flattenedActivitiesToReturn = new LinkedHashMap<String, Collection<FlattenDate>>();
//    for (String level : getLevelsForActivity(activityName)) {
//      flattenedActivitiesToReturn.put(level, getActivitiesPerTeamAndApplicationFlat(applicationName, teamName, activityName, level));
//    }
//    return flattenedActivitiesToReturn;
//  }
}

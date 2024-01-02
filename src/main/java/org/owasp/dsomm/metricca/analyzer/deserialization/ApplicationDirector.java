package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
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
    logger.info("yamlConfigurationFilePath: " + yamlScanner.getSkeletonYaml());

    ArrayList<Application> applications = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    Map<?, ?> yamlActivityFileMap = YamlReader.convertYamlToJavaYaml(yamlScanner.getSkeletonYaml().getPath());
    String skeletonString = mapper.writeValueAsString(yamlActivityFileMap.get("activity definitions"));
    logger.info("skeletonString: " + skeletonString);
    List<SkeletonActivity> skeletonActivities = mapper.readValue(skeletonString, new TypeReference<List<SkeletonActivity>>() {
    });

    for (File yamlApplicationFilePath : yamlScanner.getApplicationYamls()) {
      logger.info("yamlApplicationFilePath: " + yamlApplicationFilePath.getPath());
      Map<?, ?> applicationYamlReader = YamlReader.convertYamlToJavaYaml(yamlApplicationFilePath.getPath());
      ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
      JsonNode applicationJsonNode = objectMapper.readTree(new File(yamlApplicationFilePath.getPath()));

      assert applicationYamlReader != null;
      switch ((String) applicationYamlReader.get("kind")) { // TODO team/application
        case "application":
        case "team":
          logger.debug("applicationYamlReader" + applicationYamlReader);
          Map<String, String> settingsApplicationYamlMap = (Map<String, String>) applicationYamlReader.get("settings");
          Application newApp = new Application(applicationJsonNode.get("activities"), skeletonActivities);
          newApp.setApplication(settingsApplicationYamlMap.get("application"));
          newApp.setTeam(settingsApplicationYamlMap.get("team"));
          newApp.setDesiredLevel(settingsApplicationYamlMap.get("desired level"));
          applications.add(newApp);
          break;
        default:
          logger.error("Yaml file " + yamlApplicationFilePath.getPath() + " has no kind defined.");
          break;
      }
    }


    ApplicationDirector.applications = applications;
  }

  public List<java.util.Date> getDatesFromActivities(String activityName) throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    List<java.util.Date> datesToReturn = new ArrayList<java.util.Date>();
    for (Activity activity : this.getActivities(activityName)) {
      if (activity.getName().equals(activityName)) {
        for (Date date : activity.getDateComponents()) {
          datesToReturn.add(date.getDate());
        }
      }
    }
    Comparator<java.util.Date> dateComparator = (date1, date2) -> {
      return date1.compareTo(date2);
    };
    Collections.sort(datesToReturn, dateComparator);
    return datesToReturn;
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
    return getActivitiesPerTeamAndApplicationFlat(teamName, null, activityName);
  }

  public Collection<FlattenDate> getActivitiesPerTeamAndApplicationFlat(String teamName, String applicationName, String activityName) throws Exception {
    Collection<FlattenDate> activitiesToReturn = new ArrayList<FlattenDate>();
    // TODO with level
    //    for (Application application : this.getApplications()) {
    //      if (!application.getApplication().equals(applicationName) && applicationName != null) {
    //        logger.debug("Skipping application: " + application.getApplication() + " because it does not match: " + applicationName);
    //        continue;
    //      }
    //      for (Activity activity : application.getActivities(activityName)) {
    //        if (!teamName.equals(application.getTeam()) && teamName != null) {
    //          logger.debug("Skipping application: " + application.getApplication() + " because it does not match: " + applicationName + " and team: " + application.getTeam() + " does not match: " + teamName);
    //          continue;
    //        }
    //        logger.debug("Found activity: " + activity.getName() + " in application: " + application.getApplication());
    //        if(activity instanceof DatePeriodActivity) {
    //          for (Date dateComponent : ((DatePeriodActivity) activity).getDateComponents()) {
    //            FlattenDate flattenDate = new FlattenDate(dateComponent.getDate());
    //            flattenDate.addDynamicField(application.getTeam() + "-" + application.getApplication(), true);
    //            activitiesToReturn.add(flattenDate);
    //            if(dateComponent.getEndDate() == null ) {
    //              logger.debug("Found date which is between two dates in period, not adding: " + activity.getName() + " in application: " + application.getApplication() + " with datePeriodComponent: " + dateComponent + " team: " + application.getTeam());
    //              continue;
    //            }
    //            FlattenDate flattenDateEnd = new FlattenDate(dateComponent.getEndDate());
    //            flattenDateEnd.addDynamicField(application.getTeam() + "-" + application.getApplication(), false); // TODO BUG
    //            activitiesToReturn.add(flattenDateEnd);
    //          }
    //        }
    //      }
    //    }
    return activitiesToReturn;
  }

  public Collection<FlattenDate> getActivitiesFlat(String activityName) throws Exception {
    return getActivitiesPerTeamFlat(null, activityName);
  }
}

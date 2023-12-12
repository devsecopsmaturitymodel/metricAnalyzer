package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.exception.ComponentNotFoundException;
import org.owasp.dsomm.metricca.analyzer.exception.SkeletonNotFoundException;
import org.owasp.dsomm.metricca.analyzer.model.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DateComponent;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DatePeriodComponent;
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

  private static Collection<Application> applications = new ArrayList<>();
  @Autowired
  private YamlScanner yamlScanner;

  // Direct instantiation not possible
  private ApplicationDirector() {
  }

  public Collection<Application> getApplications() throws SkeletonNotFoundException, ComponentNotFoundException, IOException, GitAPIException {
//        if(activities == null || activities.isEmpty()) {
    yamlScanner.enforceGitCloneIfTargetFolderExists = true; // set in cronjob
    initiateApplications();
    yamlScanner.enforceGitCloneIfTargetFolderExists = false;
//        }
    return applications;
  }

  // TODO: CronJob
  private void initiateApplications() throws SkeletonNotFoundException, ComponentNotFoundException, IOException, GitAPIException {
    logger.info("yamlConfigurationFilePath: " + yamlScanner.getSkeletonYaml());
    Map<?, ?> configJavaYaml = YamlReader.convertYamlToJavaYaml(yamlScanner.getSkeletonYaml().getPath());
    ArrayList<Application> applications = new ArrayList<>();
    for (File yamlApplicationFilePath : yamlScanner.getApplicationYamls()) {
      logger.info("yamlApplicationFilePath: " + yamlApplicationFilePath.getPath());
      Map<?, ?> applicationYamlReader = YamlReader.convertYamlToJavaYaml(yamlApplicationFilePath.getPath());
      assert applicationYamlReader != null;
      switch ((String) applicationYamlReader.get("kind")) {
        case "application":
        case "team":
          logger.debug("applicationYamlReader" + applicationYamlReader);
          Map<String, String> settingsApplicationYamlMap = (Map<String, String>) applicationYamlReader.get("settings");
          Application newApp = new Application(configJavaYaml);
          newApp.setApplication(settingsApplicationYamlMap.get("application"));
          newApp.setTeam(settingsApplicationYamlMap.get("team"));
          newApp.setDesiredLevel(settingsApplicationYamlMap.get("desired level"));
          newApp.saveData(applicationYamlReader);
          applications.add(newApp);
          break;
        default:
          logger.error("Yaml file " + yamlApplicationFilePath.getPath() + " has no kind defined.");
          break;
      }
    }
    ApplicationDirector.applications = applications;
  }

  public Collection<Activity> getActivities(String activityName) {
    Collection<Activity> activitiesToReturn = new ArrayList<Activity>();
    for (Application application : applications) {
      for (Activity activity : application.getActivities()) {
        if (activity.getName().equals(activityName)) {
          activitiesToReturn.add(activity);
        }
      }
    }
    return activitiesToReturn;
  }

  public Collection<Date> getDatesFromActivities(String activityName) {
    Collection<Date> datesToReturn = new ArrayList<Date>();
    for (Activity activity : this.getActivities(activityName)) {
      if (activity.getName().equals(activityName)) {
        for (DateComponent date : activity.getDateComponents())
          datesToReturn.add(date.getValue());
      }
    }
    Comparator<Date> dateComparator = (date1, date2) -> {
      return date1.compareTo(date2);
    };
    Collections.sort((ArrayList<Date>) datesToReturn, dateComparator);
    return datesToReturn;
  }

  public Collection<FlattenDate> getActivitiesPerTeamFlat(String teamName, String activityName) throws Exception {
    return getActivitiesPerTeamAndApplicationFlat(teamName, null, activityName);
  }

  public Collection<FlattenDate> getActivitiesPerTeamAndApplicationFlat(String teamName, String applicationName, String activityName) throws Exception {
    Collection<FlattenDate> activitiesToReturn = new ArrayList<FlattenDate>();
    for (Application application : this.getApplications()) {
      if(!application.getApplication().equals(applicationName) && applicationName != null) {
        logger.debug("Skipping application: " + application.getApplication() + " because it does not match: " + applicationName);
        continue;
      }
      for (Activity activity : application.getActivities(activityName)) {
        if (!teamName.equals(application.getTeam()) && teamName != null) {
          logger.debug("Skipping application: " + application.getApplication() + " because it does not match: " + applicationName + " and team: " + application.getTeam() + " does not match: " + teamName);
          continue;
        }
          logger.debug("Found activity: " + activity.getName() + " in application: " + application.getApplication());
          for (DateComponent dateComponent : activity.getDateComponents()) {
            FlattenDate flattenDate = new FlattenDate(dateComponent.getValue());
            flattenDate.addDynamicField(application.getTeam() + "-" + application.getApplication(), ((DatePeriodComponent) dateComponent).isActive());
            activitiesToReturn.add(flattenDate);
          }

      }
    }
    return activitiesToReturn;
  }

  public Collection<FlattenDate> getActivitiesFlat(String activityName) throws Exception {
    return getActivitiesPerTeamFlat(null, activityName);
  }

}

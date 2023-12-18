package org.owasp.dsomm.metricca.analyzer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.owasp.dsomm.metricca.analyzer.controller.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.deserialization.ActivityFactory;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.UrlActivity;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.YamlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EnableConfigurationProperties
@SpringBootApplication
//@ComponentScan(basePackages = {"org.owasp.dsomm.metricCA.analyzer"})
public class MetricAnalyzerApplication {
  private static final Logger logger = LoggerFactory.getLogger(MetricAnalyzerApplication.class);

  public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    for (String arg : args) {
      logger.info("arg: " + arg);
    }


    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    Map<?, ?> yamlActivityFileMap = YamlReader.convertYamlToJavaYaml("src/main/resources/skeleton.yaml");
//    ObjectMapper mapper = new ObjectMapper();
    String skeletonString = mapper.writeValueAsString(yamlActivityFileMap.get("activity definitions"));
    logger.info("skeletonString: " + skeletonString);
    List<SkeletonActivity> skeletonActivities = mapper.readValue(skeletonString, new TypeReference<List<SkeletonActivity>>() {
    });
    Map<?, ?> activityObjects = YamlReader.convertYamlToJavaYaml("definitions/gandalf.yaml");
    List<Activity> activities = new ArrayList<>();
    for (SkeletonActivity skeletonActivity : skeletonActivities) {
      for (String activityName : skeletonActivity.getActivityNames()) {
        Class<? extends Activity> clazz = (Class<? extends Activity>) Class.forName("org.owasp.dsomm.metricca.analyzer.deserialization.activity." + skeletonActivity.getClassName());
        Object activityData = activityObjects.get(activityName);
        Activity activity = ActivityFactory.createActivity(activityName, activityData, clazz, skeletonActivity);
        activities.add(activity);
      }
    }

    for (Activity activity : activities) {
      logger.info("activity: " + activity.getName());
      logger.info("activity threshold: " + activity.getThresholds().get(0).getTargets().get(0));
      if (activity instanceof UrlActivity && ((UrlActivity) activity).getUrls() != null) {
        logger.info("url: " + ((UrlActivity) activity).getUrls().get(0));
      }
    }

//    SpringApplication.run(MetricAnalyzerApplication.class, args);
  }
}

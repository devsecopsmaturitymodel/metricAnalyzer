package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.owasp.dsomm.metricca.analyzer.controller.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.DatePeriodActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityFactory {
  private static final Logger logger = LoggerFactory.getLogger(ActivityFactory.class);


  public static Activity createActivity(String activityName, Object activityData, Class<? extends Activity> clazz, SkeletonActivity skeletonActivity) throws JsonProcessingException, InstantiationException, IllegalAccessException {
    Activity activity = deserialize(activityName, activityData, clazz);

    if(activity instanceof DatePeriodActivity) {
      ActivityDatePeriodBuilder builder = new ActivityDatePeriodBuilder((DatePeriodActivity) activity);
      builder.withName(activityName).withThresholds(skeletonActivity.getThresholds()); //TODO .withDatePeriod(skeletonActivity.getDatePeriod())
      return builder.build();
    } else {
      ActivityBuilder builder = new ActivityBuilder(activity);
      builder.withName(activityName).withThresholds(skeletonActivity.getThresholds());
      return builder.build();
    }
  }

  private static Activity deserialize(String activityName, Object activityData, Class<? extends Activity> clazz) throws JsonProcessingException, InstantiationException, IllegalAccessException {
    if(activityData == null) {
      logger.info("activityData for activityName " + activityName + " is null which means that the activity is defined as skeleton and not defined in the yaml file of the team/application. Creating a default empty activity class ." + clazz.getSimpleName());
      return clazz.newInstance();
    }
    ObjectMapper activityObjectMapper = new ObjectMapper(new YAMLFactory());
    activityObjectMapper.enableDefaultTyping();
    activityObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    activityObjectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    activityObjectMapper.registerSubtypes(new NamedType(clazz, activityName));
    Activity activity = activityObjectMapper.convertValue(activityData, clazz);

    return activity;
  }
}

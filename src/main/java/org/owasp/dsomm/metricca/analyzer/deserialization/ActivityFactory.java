package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityFactory {
  private static final Logger logger = LoggerFactory.getLogger(ActivityFactory.class);


  public static Activity createActivity(String activityName, JsonNode activityData, Class<? extends Activity> clazz, SkeletonActivity skeletonActivity) throws JsonProcessingException, InstantiationException, IllegalAccessException {
    Activity activity = deserialize(activityName, activityData, clazz);

    return buildActivity(activity, skeletonActivity, activityName);
  }

  private static Activity buildActivity(Activity activity, SkeletonActivity skeletonActivity, String activityName) throws InstantiationException, IllegalAccessException {
    ActivityBuilder builder = new ActivityBuilder(activity);
    builder
      .withName(activityName)
      .withThresholds(skeletonActivity.getThresholds())
      .withGrafanaPanelType(skeletonActivity.getGrafanaPanelType());
    return builder.build();
  }

  private static Activity deserialize(String activityName, JsonNode activityData, Class<? extends Activity> clazz) throws JsonProcessingException, InstantiationException, IllegalAccessException {
    if(activityData == null) {
      logger.info("activityData for activityName " + activityName + " is null which means that the activity is defined as skeleton and not defined in the yaml file of the team/application. Creating a default empty activity class ." + clazz.getSimpleName());
      return clazz.newInstance();
    }
    ObjectMapper activityObjectMapper = new ObjectMapper();
//    activityObjectMapper.enableDefaultTyping();
    activityObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    activityObjectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    //activityObjectMapper.registerSubtypes(new NamedType(Activity.class, activityName));
    logger.info("activityData for activityName " + activityName + " is not null. Creating a " + clazz.getSimpleName() + " class with activityData" + activityData);

    ObjectMapper mapper = new ObjectMapper();
    String activityDataString = mapper.writeValueAsString(activityData);
    logger.info("activityDataMap" + activityDataString);


//    SimpleModule module = new SimpleModule();
//    module.addDeserializer(DatePeriod.class, new ComponentDeserializer());
//    activityObjectMapper.registerModule(module);
    Activity activity = activityObjectMapper.convertValue(activityData, clazz);

    return activity;
  }
}

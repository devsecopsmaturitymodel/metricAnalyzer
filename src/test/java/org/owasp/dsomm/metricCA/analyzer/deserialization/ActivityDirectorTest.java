package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)

public class ActivityDirectorTest {
  private static final String KIND = "kind";
  ObjectMapper objectMapper = new ObjectMapper();
  private static final String jsonNodeString = """ 
                  {"settings":{"team":"team"},"kind":"kind"}
          """;

  @Test
  public void shouldGetActivities() throws JsonProcessingException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    List<SkeletonActivity> skeletonActivities = new ArrayList<>();
    SkeletonActivity skeletonActivity = new SkeletonActivity();
    skeletonActivity.setKind(KIND);
    List<String> activitiesList = new ArrayList<>();
    activitiesList.add("UrlActivity");
    skeletonActivity.setClassName("UrlActivity");
    skeletonActivity.setActivityNames(activitiesList);
    skeletonActivities.add(skeletonActivity);

    ActivityDirector activityDirector1 = new ActivityDirector(objectMapper.readTree(jsonNodeString), skeletonActivities, KIND);
    assertThat(activityDirector1.getActivities())
            .isNotNull()
            .hasSize(1);
  }

  @Test
  public void shouldGetActivitiesWhenKindIsEqual() throws JsonProcessingException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    List<SkeletonActivity> skeletonActivities = new ArrayList<>();
    SkeletonActivity skeletonActivity = new SkeletonActivity();
    skeletonActivity.setKind(KIND);
    List<String> activitiesList = new ArrayList<>();
    activitiesList.add("UrlActivity");
    skeletonActivity.setClassName("UrlActivity");
    skeletonActivity.setActivityNames(activitiesList);
    skeletonActivities.add(skeletonActivity);

    ActivityDirector activityDirector1 = new ActivityDirector(objectMapper.readTree(jsonNodeString), skeletonActivities, "KIND");
    assertThat(activityDirector1.getActivities())
            .isNotNull();
  }
}

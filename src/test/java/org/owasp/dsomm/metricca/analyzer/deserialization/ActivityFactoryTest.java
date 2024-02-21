package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.deserialization.ActivityFactory;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.UrlActivity;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ActivityFactoryTest {

  private static final String ACTIVITY_NAME = "activity name";
  private static final String jsonNodeString = """
          {
            "components": [
              {
                "url": "http://example.com",
                "date": "2024-02-07"
              },
              {
                "url": "http://example.org",
                "date": "2024-02-08"
              }
            ]
          }
          """;

  ObjectMapper objectMapper = new ObjectMapper();

  @Mock
  private Activity activity;
  @Mock
  private SkeletonActivity skeletonActivity;
  @InjectMocks
  private ActivityFactory activityFactory;

  @Test
  public void shouldCreateActivity() throws JsonProcessingException, InstantiationException, IllegalAccessException {
    assertThat(ActivityFactory.createActivity(ACTIVITY_NAME, objectMapper.readTree(jsonNodeString), UrlActivity.class,
            skeletonActivity))
            .isNotNull();
  }

  @Test
  public void shouldCreateActivityWhenActivityDataIsNull() throws JsonProcessingException, InstantiationException,
          IllegalAccessException {
    assertThat(ActivityFactory.createActivity(ACTIVITY_NAME, null, UrlActivity.class, skeletonActivity))
            .isNotNull();
  }

}

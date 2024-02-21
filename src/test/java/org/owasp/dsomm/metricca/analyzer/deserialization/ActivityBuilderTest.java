package org.owasp.dsomm.metricca.analyzer.deserialization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.deserialization.ActivityBuilder;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Threshold;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ActivityBuilderTest {
  private static final String NAME = "Metric";
  private static final String KIND = "kind";

  @Mock
  private Activity activity;
  @Mock
  private Threshold threshold;
  @InjectMocks
  private ActivityBuilder activityBuilder;

  @Test
  public void withNameTest() {
    assertThat(activityBuilder.withName(NAME))
            .isNotNull();

    verify(activity).setName(NAME);
  }

  @Test
  public void withThresholdsTest() {
    assertThat(activityBuilder.withThresholds(List.of(threshold)))
            .isNotNull();

    verify(activity).setThresholds(List.of(threshold));
  }

  @Test
  public void withKindTest() {
    assertThat(activityBuilder.withKind(KIND))
            .isNotNull();

    verify(activity).setKind(KIND);
  }

  @Test
  public void buildTest() {
    assertThat(activityBuilder.build())
            .isNotNull();

    verify(activity).finishActivity();
  }

}

package org.owasp.dsomm.metricca.analyzer.grafana;

import org.junit.jupiter.api.Test;
import org.owasp.dsomm.metricca.analyzer.ApplicationTest;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PanelTest extends ApplicationTest {
  private static final Logger logger = LoggerFactory.getLogger(PanelTest.class);

  @Test
  public void testPanel() throws Exception {
    Map<String, PanelConfiguration> panelConfigurations = new HashMap<String, PanelConfiguration>();
    boolean found = false;
    String activityName = "Security requirements";
    for (SkeletonActivity skeletonActivity : ApplicationDirector.getSkeletonActivities()) {
      if (skeletonActivity.getActivityNames().contains("Security requirements")) {
        for (PanelConfiguration panelConfiguration : skeletonActivity.getPanelConfigurations()) {
          if (activityName.equals(panelConfiguration.getTitle())) {
            found = true;
            break;
          }
        }
      }
    }
    assertTrue(found, "Panel configuration for activity " + activityName + " not found");
  }
}

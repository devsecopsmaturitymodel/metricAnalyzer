package org.owasp.dsomm.metricca.analyzer.deserialization;

import org.junit.jupiter.api.Test;
import org.owasp.dsomm.metricca.analyzer.ApplicationTest;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.grafana.PanelConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TargetTest extends ApplicationTest {
  private static final Logger logger = LoggerFactory.getLogger(TargetTest.class);

  @Test
  public void testCountTargetHasDescription() throws Exception {
    Map<String, PanelConfiguration> panelConfigurations = new HashMap<String, PanelConfiguration>();
    boolean found = false;
    String activityName = "Security requirements";
    assert(applicationDirector.getSkeletonActivities().size() > 0);
    for (SkeletonActivity skeletonActivity : applicationDirector.getSkeletonActivities()) {
      if (skeletonActivity.getActivityNames().contains("Security requirements")) {
        for (PanelConfiguration panelConfiguration : skeletonActivity.getPanelConfigurations()) {
          if (panelConfiguration.getDescription() != null) {
            found = true;
            break;
          } else {
            logger.warn("Panel description for activity " + activityName + " not set while panel exists");
          }
        }
      } else {
        logger.error("Activity " + activityName + " not found in " + skeletonActivity.getClassName());
      }
    }
    assertTrue(found, "Panel description for activity " + activityName + " not found");
  }
}

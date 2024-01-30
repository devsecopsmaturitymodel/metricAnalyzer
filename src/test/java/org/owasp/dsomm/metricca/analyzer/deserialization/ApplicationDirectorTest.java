package org.owasp.dsomm.metricca.analyzer.deserialization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.owasp.dsomm.metricca.analyzer.ApplicationTest;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationDirectorTest extends ApplicationTest {
  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testApplicationsExists() throws Exception {
    List<Application> actualApplications = applicationDirector.getApplications();
    assertTrue(actualApplications.size() > 0);
  }

  @Test
  public void testTeamActivitySecurityRequirementsExists() throws Exception {
    List<Application> actualApplications = applicationDirector.getApplications();
    boolean foundSecurityRequirements = false;
    for (Application application : actualApplications) {
      if(!application.getTeam().equals("nix-team")) {
        continue;
      }
      for (Activity activity : application.getActivities()) {
        if (activity.getName().equals("Security requirements")) {
          foundSecurityRequirements = true;
          break;
        }
      }
    }
    assertTrue(foundSecurityRequirements);
  }
}

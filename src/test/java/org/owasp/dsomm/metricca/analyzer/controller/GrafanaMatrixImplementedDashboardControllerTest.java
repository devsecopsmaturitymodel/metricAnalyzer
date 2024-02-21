package org.owasp.dsomm.metricca.analyzer.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.controller.GrafanaMatrixDashboardController;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrafanaMatrixImplementedDashboardControllerTest {

  private static final String TEAM = "team";
  private static final String DESIRE_LEVEL = "level";
  private static final String MAP_KEY = "key";

  @Mock
  private ApplicationDirector applicationDirector;
  @Mock
  private Application application;
  @Mock
  private Activity activity;
  @InjectMocks
  private GrafanaMatrixDashboardController grafanaMatrixDashboardController;

  @Test
  public void shouldGetOverviewDashboard() throws Exception {
    when(applicationDirector.getApplications()).thenReturn(List.of(application));
    when(application.getTeam()).thenReturn(TEAM);
    when(application.getDesiredLevel()).thenReturn(DESIRE_LEVEL);
    when(application.getActivities()).thenReturn(List.of(activity));
    Map<String, Boolean> map = new HashMap<>();
    map.put(MAP_KEY, Boolean.TRUE);
    when(activity.isActivityImplemented()).thenReturn(map);

    assertThat(grafanaMatrixDashboardController.getOverviewDashboardDesiredLevel()).isNotNull();

    verify(applicationDirector).getApplications();
  }

  @Test
  public void shouldRunIfBlockGetOverviewDashboard() throws Exception {
    when(applicationDirector.getApplications()).thenReturn(List.of(application));
    when(application.getTeam()).thenReturn(TEAM);
    when(application.getActivities()).thenReturn(List.of(activity));
    Map<String, Boolean> map = new HashMap<>();
    map.put(MAP_KEY, Boolean.TRUE);
    when(activity.isActivityImplemented()).thenReturn(map);

    assertThat(grafanaMatrixDashboardController.getOverviewDashboardDesiredLevel()).isNotNull();

    verify(applicationDirector).getApplications();
  }

}

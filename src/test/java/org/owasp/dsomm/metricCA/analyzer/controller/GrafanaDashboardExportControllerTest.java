package org.owasp.dsomm.metricca.analyzer.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.grafana.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrafanaDashboardExportControllerTest {
  private static final String TYPE = "team";
  private static final String ERROR_MESSAGE = "404 NOT_FOUND \"Dashboard not found\"";

  @Mock
  private OverviewDashboard overviewDashboard;
  @Mock
  private PanelConfiguration panelConfiguration;
  @Mock
  private PanelFactory panelFactory;
  @Mock
  private TeamDashboard teamDashboard;
  @Mock
  private ApplicationDirector applicationDirector;
  @Mock
  private GrafanaDashboardCreator grafanaDashboardCreator;
  @Mock
  private SkeletonActivity skeletonActivity;
  @Mock
  HashMap<String, String> map;
  @InjectMocks
  private GrafanaDashboardExportController grafanaDashboardExportController;

  @Test
  public void shouldGetOverviewDashboard() throws Exception {
    when(grafanaDashboardCreator.getDashboards()).thenReturn(new HashMap<>(){{ put(TYPE, TYPE); }});

    assertThat(grafanaDashboardExportController.getOverviewDashboard(TYPE)).isNotEmpty();

    verify(grafanaDashboardCreator).getDashboards();
  }

  @Test
  public void shouldThrowExceptionGetOverviewDashboard() throws Exception {
    when(grafanaDashboardCreator.getDashboards()).thenReturn(map);

    assertThatThrownBy(() -> grafanaDashboardExportController.getOverviewDashboard(TYPE))
            .isInstanceOf(ResponseStatusException.class).extracting(ResponseStatusException.class::cast)
            .returns(ERROR_MESSAGE, Throwable::getMessage);

    verify(grafanaDashboardCreator).getDashboards();
  }

  @Test
  public void shouldGetTeamDashboard() throws Exception {
    HashMap<String, String> map1 = new HashMap<>();
    map1.put(TYPE, TYPE);
    when(grafanaDashboardCreator.getDashboards()).thenReturn(map1);

    assertThat(grafanaDashboardExportController.getTeamDashboard()).isNotEmpty();
  }

  @Test
  public void shouldPushOverviewDashboard() throws Exception {
    when(grafanaDashboardCreator.pushDashboards()).thenReturn(Boolean.TRUE);

    assertThat(grafanaDashboardExportController.pushOverviewDashboard()).isEqualTo("{\"status\": \"pushed\"}");

    verify(grafanaDashboardCreator).pushDashboards();
  }

  @Test
  public void shouldReturnErrorPushOverviewDashboard() throws Exception {
    when(grafanaDashboardCreator.pushDashboards()).thenReturn(Boolean.FALSE);

    assertThat(grafanaDashboardExportController.pushOverviewDashboard()).isEqualTo("{\"status\": \"error\"}");

    verify(grafanaDashboardCreator).pushDashboards();
  }

  @Test
  public void shouldThrowExceptionPushOverviewDashboard() throws Exception {
    when(grafanaDashboardCreator.pushDashboards()).thenThrow(Exception.class);

    assertThat(grafanaDashboardExportController.pushOverviewDashboard()).isEqualTo("{\"status\": \"error\"}");

    verify(grafanaDashboardCreator).pushDashboards();
  }
}

package org.owasp.dsomm.metricca.analyzer.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.grafana.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrafanaDashboardExportControllerTest {
  private static final String MAP_KEY = "abc";
  private static final String TITLE = "title";

  @Mock
  private OverviewDashboard overviewDashboard;
  @Mock
  PanelConfiguration panelConfiguration;
  @Mock
  PanelFactory panelFactory;
  @Mock
  private TeamDashboard teamDashboard;
  @Mock
  @Autowired
  private ApplicationDirector applicationDirector;
  @Mock
  private GrafanaDashboardCreator grafanaDashboardCreator;
  @Mock
  private SkeletonActivity skeletonActivity;
  @InjectMocks
  private GrafanaDashboardExportController grafanaDashboardExportController;

  @Test
  public void getOverviewDashboard() throws Exception {
    assertThat(grafanaDashboardExportController.getOverviewDashboard()).isNull();
  }

  @Test
  public void shouldGetOverviewDashboardTest() throws Exception {
    try (MockedStatic<ApplicationDirector> mockedFactory = Mockito.mockStatic(ApplicationDirector.class);
         MockedStatic<PanelFactory> panelFactory = Mockito.mockStatic(PanelFactory.class)) {
      when(panelConfiguration.getTitle()).thenReturn(TITLE);
      Map<String, PanelConfiguration> map = new HashMap<>();
      map.put(MAP_KEY, panelConfiguration);
      mockedFactory.when(ApplicationDirector::getSkeletonActivities).thenReturn(List.of(skeletonActivity));
      mockedFactory.when(() -> PanelFactory.getPanelsForLevels(panelConfiguration, skeletonActivity)).thenReturn(map);
      when(skeletonActivity.getPanelConfigurations()).thenReturn(List.of(panelConfiguration));

      assertThat(grafanaDashboardExportController.getOverviewDashboard()).isNull();
    }
  }

  @Test
  public void shouldGetTeamDashboard(){
    try (MockedStatic<ApplicationDirector> mockedFactory = Mockito.mockStatic(ApplicationDirector.class);
         MockedStatic<PanelFactory> panelFactory = Mockito.mockStatic(PanelFactory.class)) {
      mockedFactory.when(ApplicationDirector::getSkeletonActivities).thenReturn(List.of(skeletonActivity));
      when(skeletonActivity.getPanelConfigurations()).thenReturn(List.of(panelConfiguration));
      Map<String, PanelConfiguration> map = new HashMap<>();
      map.put(MAP_KEY, panelConfiguration);
      mockedFactory.when(() -> PanelFactory.getPanelsForLevels(panelConfiguration, skeletonActivity)).thenReturn(map);
      when(panelConfiguration.getTitle()).thenReturn(TITLE);

      assertThat(grafanaDashboardExportController.getTeamDashboard()).isNull();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void shouldPushOverviewDashboard() throws Exception {
      when(panelConfiguration.getTitle()).thenReturn(TITLE);
      when(overviewDashboard.getDashboard(List.of(panelConfiguration))).thenReturn("dashboard");
      when(grafanaDashboardCreator.pushDashboard()).thenReturn(Boolean.TRUE);

      assertThat(grafanaDashboardExportController.pushOverviewDashboard()).isNotEmpty();
  }

}

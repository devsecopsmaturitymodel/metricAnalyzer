package org.owasp.dsomm.metricca.analyzer.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrafanaTeamDashboardControllerTest {
  private static final String ACTIVITY_NAME = "abc activity";
  private static final String APPLICATION = "abc application";
  private static final String TEAM_NAME = "abc level";

  @Mock
  private ApplicationDirector applicationDirector;
  @Mock
  private Application application;
  @Mock
  private FlattenDate flattenDate;
  @Mock
  private LinkedHashMap<String, Collection<FlattenDate>> linkedHashMap;
  @InjectMocks
  GrafanaTeamDashboardController grafanaTeamDashboardController;

  @Test
  public void shouldGetActivitiesPerTeamFlatSimple() throws Exception {
    when(applicationDirector.getActivitiesPerTeamAndApplicationFlat(APPLICATION, TEAM_NAME, ACTIVITY_NAME))
            .thenReturn(List.of(flattenDate));

    assertThat(grafanaTeamDashboardController.getActivitiesPerTeamFlatSimple(TEAM_NAME, APPLICATION, ACTIVITY_NAME))
            .isNotNull()
            .hasSize(1);
  }

  @Test
  public void shouldGetActivitiesPerTeamFlatAsMap() throws Exception {
    when(applicationDirector.getActivitiesPerTeamAndApplicationFlatAsLevelMap(APPLICATION, TEAM_NAME, ACTIVITY_NAME))
            .thenReturn(linkedHashMap);

    assertThat(grafanaTeamDashboardController.getActivitiesPerTeamFlatAsMap(TEAM_NAME, APPLICATION, ACTIVITY_NAME))
            .isNotNull()
            .hasSize(0);
  }
}

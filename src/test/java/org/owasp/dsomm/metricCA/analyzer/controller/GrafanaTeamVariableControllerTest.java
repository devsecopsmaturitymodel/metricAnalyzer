package org.owasp.dsomm.metricca.analyzer.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrafanaTeamVariableControllerTest {
  private static final String TEAM_NAME = "abc level";

  @Mock
  private ApplicationDirector applicationDirector;
  @Mock
  private Application application;
  @InjectMocks
  GrafanaTeamVariableController grafanaTeamVariableController;

  @Test
  public void shouldGetTeams() throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    when(applicationDirector.getApplications()).thenReturn(List.of(application));

    assertThat(grafanaTeamVariableController.getTeams())
            .isNotNull()
            .hasSize(1);
  }

  @Test
  public void getTeamApplicationIds() throws Exception {
    when(applicationDirector.getApplications()).thenReturn(List.of(application));
    when(application.getTeam()).thenReturn(TEAM_NAME);

    assertThat(grafanaTeamVariableController.getTeamApplicationIds(TEAM_NAME))
            .isNotNull()
            .hasSize(1);
  }
}

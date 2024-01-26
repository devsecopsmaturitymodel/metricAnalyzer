package org.owasp.dsomm.metricca.analyzer.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.exception.SkeletonNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrafanaControllerTest {

  private static final String ERROR_MESSAGE = "error";
  private static final String TEAM_NAME = "abc team";
  private static final String APPLICATION_ID = "abc id";
  private static final String ACTIVITY_NAME = "abc activity";

  @Mock
  private ApplicationDirector applicationDirector;
  @Mock
  private Application application;
  @Mock
  private Activity activity;
  @Mock
  private FlattenDate flattenDates;
  @InjectMocks
  private GrafanaController grafanaController;

  @Test
  public void shouldGetActivities() throws Exception {
    when(applicationDirector.getApplications()).thenReturn(List.of(application));
    when(application.getActivities()).thenReturn(List.of(activity));

    assertThat(grafanaController.getActivities())
            .isNotEmpty()
            .hasSize(1);
  }

  @Test
  public void shouldThrowsExceptionOnGetActivities() throws Exception {
    when(applicationDirector.getApplications()).thenThrow(new SkeletonNotFoundException(ERROR_MESSAGE));

    assertThatThrownBy(() -> grafanaController.getActivities()).isInstanceOf(SkeletonNotFoundException.class).extracting(SkeletonNotFoundException.class::cast).returns(ERROR_MESSAGE, Throwable::getMessage);

    verify(applicationDirector).getApplications();
  }

  @Test
  public void shouldGetApplication() throws Exception {
    when(applicationDirector.getApplications()).thenReturn(List.of(application));

    assertThat(grafanaController.getApplications())
            .isNotEmpty()
            .hasSize(1);

    verify(applicationDirector).getApplications();
  }

  @Test
  public void shouldGetTeamApplication() throws Exception {
    when(application.getTeam()).thenReturn(TEAM_NAME);
    when(applicationDirector.getApplications()).thenReturn(List.of(application));

    assertThat(grafanaController.getTeamApplications(TEAM_NAME))
            .hasSize(1);

    verify(applicationDirector).getApplications();
    verify(application).getTeam();
  }

  @Test
  public void shouldGetActivitiesFlatSimple() throws Exception {
    when(applicationDirector.getActivitiesFlat(ACTIVITY_NAME)).thenReturn(List.of(flattenDates));

    assertThat(grafanaController.getActivitiesFlatSimple(ACTIVITY_NAME))
            .hasSize(1);
  }

  @Test
  public void shouldGetTeamActivity() throws Exception {
    when(applicationDirector.getApplications()).thenReturn(List.of(application));
    when(application.getTeam()).thenReturn(TEAM_NAME);
    when(application.getApplication()).thenReturn(APPLICATION_ID);
    when(application.getActivities()).thenReturn(List.of(activity));
    when(activity.getName()).thenReturn(ACTIVITY_NAME);

    assertThat(grafanaController.getTeamActivity(TEAM_NAME, APPLICATION_ID, ACTIVITY_NAME))
            .isNotNull()
            .asList()
            .hasSize(1);

    verify(application).getActivities();
    verify(applicationDirector).getApplications();

  }

}

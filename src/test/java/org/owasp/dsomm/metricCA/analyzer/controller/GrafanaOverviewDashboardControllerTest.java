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
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.SecurityTrainingActivity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.DatePeriodHoursAndPeople;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.ThresholdDatePeriodManager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrafanaOverviewDashboardControllerTest {
  private static final String ACTIVITY_NAME = "abc activity";
  private static final String LEVEL = "abc level";
  private static final String TEAM_NAME = "abc level";
  private static final String ERROR_MESSAGE = "SecurityTrainingActivity not implemented yet";

  @Mock
  private ApplicationDirector applicationDirector;
  @Mock
  private Activity activity;
  @Mock
  private SecurityTrainingActivity securityTrainingActivity;
  @Mock
  private FlattenDate flattenDates;
  @Mock
  private Date date;
  @Mock
  private DatePeriodHoursAndPeople datePeriodHoursAndPeople;
  @Mock
  private ThresholdDatePeriodManager thresholdDatePeriodManager;
  @Mock
  private Application application;
  @InjectMocks
  GrafanaOverviewDashboardController grafanaOverviewDashboardController;

  @Test
  public void shouldGetActivitiesSimpleNoDate() throws Exception {
    when(applicationDirector.getApplications()).thenReturn(List.of(application));
    when(application.getActivities(ACTIVITY_NAME)).thenReturn(List.of(activity));
    when(activity.getName()).thenReturn(ACTIVITY_NAME);

    assertThat(grafanaOverviewDashboardController.getActivitiesSimpleNoDate(ACTIVITY_NAME))
            .isNotEmpty()
            .hasSize(1);
  }

  @Test
  public void shouldGetActivitiesWithCount() throws Exception {
    when(applicationDirector.getApplications()).thenReturn(List.of(application));
    when(application.getActivities(ACTIVITY_NAME)).thenReturn(List.of(activity));
    when(application.getTeam()).thenReturn(TEAM_NAME);
    HashMap<String, ThresholdDatePeriodManager> map = new HashMap<>();
    map.put(LEVEL, thresholdDatePeriodManager);
    when(activity.getThresholdDatePeriodMap()).thenReturn(map);
    when(map.get(LEVEL).getThresholdValue()).thenReturn(2);

    assertThat(grafanaOverviewDashboardController.getActivitiesWithCount(ACTIVITY_NAME, LEVEL))
            .isNotEmpty()
            .hasSize(1);
  }

  @Test
  public void shouldGetActivitiesFlat() throws Exception {
    when(applicationDirector.getActivitiesPerTeamAndApplicationFlat(null, null, ACTIVITY_NAME, LEVEL))
            .thenReturn(List.of(flattenDates));

    assertThat(grafanaOverviewDashboardController.getActivitiesFlat(ACTIVITY_NAME, LEVEL))
            .isNotNull()
            .hasSize(1);
  }

  @Test
  public void shouldThrowsExceptionOnGetActivitiesHoursAndPeople() throws Exception {
    when(applicationDirector.getStartDateFromActivitiesAsMap(ACTIVITY_NAME)).thenReturn(List.of(date));
    when(applicationDirector.getApplications()).thenReturn(List.of(application));
    when(application.getActivities(ACTIVITY_NAME)).thenReturn(List.of(activity));
    when(activity.getName()).thenReturn(ACTIVITY_NAME);

    assertThatThrownBy(() -> grafanaOverviewDashboardController.getActivitiesHoursAndPeople(ACTIVITY_NAME, LEVEL)).returns(ERROR_MESSAGE, Throwable::getMessage);

  }

  @Test
  public void shouldGetActivitiesHoursAndPeople() throws Exception {
    when(applicationDirector.getStartDateFromActivitiesAsMap(ACTIVITY_NAME)).thenReturn(List.of(date));
    when(applicationDirector.getApplications()).thenReturn(List.of(application));
    when(application.getActivities(ACTIVITY_NAME)).thenReturn(List.of(securityTrainingActivity));
    when(application.getTeam()).thenReturn(TEAM_NAME);
    when(securityTrainingActivity.getName()).thenReturn(ACTIVITY_NAME);
    when(securityTrainingActivity.getLearningTimePerDate()).thenReturn(List.of(datePeriodHoursAndPeople));
    when(datePeriodHoursAndPeople.getDate()).thenReturn(date);
    when(datePeriodHoursAndPeople.getHours()).thenReturn(1);

    assertThat(grafanaOverviewDashboardController.getActivitiesHoursAndPeople(ACTIVITY_NAME, LEVEL))
            .isNull();
  }
}

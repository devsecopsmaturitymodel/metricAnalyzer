package org.owasp.dsomm.metricCA.analyzer.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricCA.analyzer.exception.SkeletonNotFoundException;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Activity;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Application;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.YamlToObjectManager;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DateComponent;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DatePeriodComponent;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class GrafanaControllerTest {

    private static final String ERROR_MESSAGE = "error";
    private static final String TEAM_NAME = "abc team";
    private static final String APPLICATION_ID = "abc id";
    private static final String ACTIVITY_NAME = "abc activity";

    @Mock
    private YamlToObjectManager yamlToObjectManager;
    @Mock
    private Application application;
    @Mock
    private Activity activity;
    @Mock
    private Date date;
    @Mock
    private DateComponent dateComponent;
    @Mock
    private DatePeriodComponent datePeriodComponent;
    @InjectMocks
    private GrafanaController grafanaController;

    @Test
    public void shouldGetActivities() throws Exception {
        when(application.getActivities()).thenReturn(List.of(activity));
        when(yamlToObjectManager.getApplications()).thenReturn(List.of(application));

        assertThat(grafanaController.getActivities()).isNotEmpty();

        verify(yamlToObjectManager).getApplications();
    }

    @Test
    public void shouldThrowsExceptionOnGetActivities() throws Exception {
        when(yamlToObjectManager.getApplications()).thenThrow(new SkeletonNotFoundException(ERROR_MESSAGE));

        assertThatThrownBy(() -> grafanaController.getActivities())
                .isInstanceOf(SkeletonNotFoundException.class)
                .extracting(SkeletonNotFoundException.class::cast)
                .returns(ERROR_MESSAGE, Throwable::getMessage);

        verify(yamlToObjectManager).getApplications();
    }

    @Test
    public void shouldGetApplicaton() throws Exception {
        when(yamlToObjectManager.getApplications()).thenReturn(List.of(application));

        assertThat(grafanaController.getApplications());

        verify(yamlToObjectManager).getApplications();
    }

    @Test
    public void shouldGetTeamApplication() throws Exception {
        when(application.getTeam()).thenReturn(TEAM_NAME);
        when(yamlToObjectManager.getApplications()).thenReturn(List.of(application));

        assertThat(grafanaController.getTeamApplications(TEAM_NAME));

        verify(yamlToObjectManager).getApplications();
        verify(application).getTeam();
    }

    @Test
    public void shouldGetTeamApplicationIds() throws Exception {
        when(yamlToObjectManager.getApplications()).thenReturn(List.of(application));
        when(application.getTeam()).thenReturn(TEAM_NAME);
        when(application.getApplicationId()).thenReturn(APPLICATION_ID);

        assertThat(grafanaController.getTeamApplicationIds(TEAM_NAME));

        verify(yamlToObjectManager).getApplications();
        verify(application).getTeam();
        verify(application).getApplicationId();
    }

    @Test
    public void shouldGetApplicationIds() throws GitAPIException, IOException {
        when(yamlToObjectManager.getApplications()).thenReturn(List.of(application));
        when(application.getApplicationId()).thenReturn(APPLICATION_ID);

        assertThat(grafanaController.getApplicationIds());

        verify(application).getApplicationId();
        verify(yamlToObjectManager).getApplications();
    }

    @Test
    public void shouldGetTeam() throws GitAPIException, IOException {
        when(yamlToObjectManager.getApplications()).thenReturn(List.of(application));
        when(application.getTeam()).thenReturn(TEAM_NAME);

        assertThat(grafanaController.getTeams());

        verify(yamlToObjectManager).getApplications();
        verify(application).getTeam();
    }

    @Test
    public void shouldGetTeamActivity() throws Exception {
        when(yamlToObjectManager.getApplications()).thenReturn(List.of(application));
        when(application.getTeam()).thenReturn(TEAM_NAME);
        when(application.getApplicationId()).thenReturn(APPLICATION_ID);
        when(application.getActivities()).thenReturn(List.of(activity));
        when(activity.getName()).thenReturn(ACTIVITY_NAME);

        assertThat(grafanaController.getTeamActivity(TEAM_NAME,APPLICATION_ID,ACTIVITY_NAME));

        verify(application).getActivities();
        verify(yamlToObjectManager).getApplications();

    }

    @Test
    public void shouldGetActivitiesFlat() throws Exception {
        when(yamlToObjectManager.getDatesFromActivities(ACTIVITY_NAME)).thenReturn(List.of(date));
        when(yamlToObjectManager.getApplications()).thenReturn(List.of(application));
        when(application.getActivities(ACTIVITY_NAME)).thenReturn(List.of(activity));
        when(activity.getDateComponents()).thenReturn(List.of(dateComponent));
        when(dateComponent.getValue()).thenReturn(date);

        assertThat(grafanaController.getActivitiesFlat(ACTIVITY_NAME));

        verify(yamlToObjectManager).getDatesFromActivities(ACTIVITY_NAME);
        verify(yamlToObjectManager).getApplications();
        verify(application).getActivities(ACTIVITY_NAME);

    }

    @Test
    public void shouldGetActivitiesFlatUsingDatePeriodComponent() throws Exception {
        when(yamlToObjectManager.getDatesFromActivities(ACTIVITY_NAME)).thenReturn(List.of(date));
        when(yamlToObjectManager.getApplications()).thenReturn(List.of(application));
        when(application.getActivities(ACTIVITY_NAME)).thenReturn(List.of(activity));
        when(activity.getDateComponents()).thenReturn(List.of(datePeriodComponent));
        when(datePeriodComponent.getValue()).thenReturn(date);
        when(activity.getDatePeriodOrEndComponent()).thenReturn(datePeriodComponent);

        assertThat(grafanaController.getActivitiesFlat(ACTIVITY_NAME));

        verify(yamlToObjectManager).getDatesFromActivities(ACTIVITY_NAME);
        verify(yamlToObjectManager).getApplications();
        verify(application).getActivities(ACTIVITY_NAME);

    }

    @Test
    public void shouldGetActivitiesFlatReturnNullInPrivateMethod() throws Exception {
        when(yamlToObjectManager.getDatesFromActivities(ACTIVITY_NAME)).thenReturn(List.of(date));
        when(yamlToObjectManager.getApplications()).thenReturn(List.of(application));
        when(application.getActivities(ACTIVITY_NAME)).thenReturn(List.of(activity));

        assertThat(grafanaController.getActivitiesFlat(ACTIVITY_NAME));

        verify(yamlToObjectManager).getDatesFromActivities(ACTIVITY_NAME);
        verify(yamlToObjectManager).getApplications();
        verify(application).getActivities(ACTIVITY_NAME);

    }
}

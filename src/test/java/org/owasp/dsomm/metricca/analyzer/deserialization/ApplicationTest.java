package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.deserialization.ActivityDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.exception.SkeletonNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationTest {
  private static final String KIND = "kind";

  private List<SkeletonActivity> skeletonActivities;

  @Mock
  private JsonNode applicationYamlReader;
  @Mock
  private Activity activity;
  @Mock
  private ActivityDirector activityDirector;
  @Mock
  private ObjectMapper activityObjectMapper;
  @Mock
  private SkeletonActivity skeletonActivity;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    skeletonActivities = new ArrayList<>();
    skeletonActivities.add(skeletonActivity);
  }

  @Test
  void testGetActivities() throws SkeletonNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    when(skeletonActivity.getKind()).thenReturn(KIND);

    Application application = new Application(applicationYamlReader, skeletonActivities, KIND);

    assertEquals(0, application.getActivities().size());
  }

  @Test
  public void shouldGetActivities() {
    try (MockedConstruction<ActivityDirector> mockPaymentService =
                 Mockito.mockConstruction(ActivityDirector.class, (mock, context) -> {
                   when(mock.getActivities()).thenReturn(List.of(activity));
                 })) {
      Application application = new Application(applicationYamlReader, skeletonActivities, KIND);

      assertThat(application.getActivities())
              .isNotNull()
              .hasSize(1);
    } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void shouldGetActivitiesByActivityName() {
    try (MockedConstruction<ActivityDirector> mockPaymentService =
                 Mockito.mockConstruction(ActivityDirector.class, (mock, context) -> {
                   when(mock.getActivities()).thenReturn(List.of(activity));
                 })) {
      Application application = new Application(applicationYamlReader, skeletonActivities, KIND);
      when(activity.getName()).thenReturn("activity");
      assertThat(application.getActivities("activity"))
              .isNotNull()
              .hasSize(1);
    } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void shouldGetTeam() {
    try (MockedConstruction<ActivityDirector> mockPaymentService =
                 Mockito.mockConstruction(ActivityDirector.class, (mock, context) ->
                         when(mock.getActivities()).thenReturn(List.of(activity)))) {

      Application application = new Application(applicationYamlReader, skeletonActivities, KIND);
      application.setTeam("team");

      assertThat(application.getTeam())
              .isNotNull();
    } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void shouldGetName() {
    try (MockedConstruction<ActivityDirector> mockPaymentService =
                 Mockito.mockConstruction(ActivityDirector.class, (mock, context) ->
                         when(mock.getActivities()).thenReturn(List.of(activity)))) {

      Application application = new Application(applicationYamlReader, skeletonActivities, KIND);
      application.setName("application");

      assertThat(application.getName())
              .isNotNull();
    } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void shouldGetDesiredLevel() {
    try (MockedConstruction<ActivityDirector> mockPaymentService =
                 Mockito.mockConstruction(ActivityDirector.class, (mock, context) ->
                         when(mock.getActivities()).thenReturn(List.of(activity)))) {

      Application application = new Application(applicationYamlReader, skeletonActivities, KIND);
      application.setDesiredLevel("level");

      assertThat(application.getDesiredLevel())
              .isNotNull();
    } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

}

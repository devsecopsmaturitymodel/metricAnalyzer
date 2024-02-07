package org.owasp.dsomm.metricca.analyzer.deserialization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TeamTest {

  @InjectMocks
  private Team team;

  @Test
  public void shouldGetName() {
    team.setName("team");
    assertThat(team.getName())
            .isNotEmpty()
            .isEqualTo("team");
  }

  @Test
  public void shouldGetApplications() {
    List<String> applications = new ArrayList<>();
    applications.add("team");
    team.setApplications(applications);
    assertThat(team.getApplications())
            .isNotNull()
            .hasSize(1);
  }

  @Test
  public void shouldGetApplicationsWithEmptyList() {
    team.setApplications(null);
    assertThat(team.getApplications())
            .isNotNull()
            .isEmpty();
  }

}

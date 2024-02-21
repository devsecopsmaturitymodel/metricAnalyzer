package org.owasp.dsomm.metricca.analyzer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.controller.GrafanaApplicationController;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrafanaApplicationControllerTest {
  @Mock
  private ApplicationDirector applicationDirector;
  @Mock
  private Application application;
  @Mock
  private ObjectMapper objectMapper;
  @Mock
  private JsonSchema jonSchema;
  @Mock
  private SchemaFactoryWrapper schemaFactoryWrapper;
  @InjectMocks
  private GrafanaApplicationController grafanaApplicationController;

  @Test
  public void shouldGetApplicationIds() throws GitAPIException, IOException, ClassNotFoundException,
          InstantiationException, IllegalAccessException {
    when(applicationDirector.getApplications()).thenReturn(List.of(application));

    assertThat(grafanaApplicationController.getApplicationIds())
            .isNotEmpty()
            .hasSize(1);

    verify(applicationDirector).getApplications();
  }

  @Test
  public void shouldGetApplicationSchema() throws IOException, GitAPIException, ClassNotFoundException,
          InstantiationException, IllegalAccessException {

    assertThat(grafanaApplicationController.getApplicationSchema()).isNotEmpty();
  }

}

package org.owasp.dsomm.metricca.analyzer;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.YamlScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ApplicationTest {
  protected ApplicationDirector applicationDirector;
  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @BeforeEach
  public void setUp() throws Exception {
    Constructor<ApplicationDirector> constructor = ApplicationDirector.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    applicationDirector = constructor.newInstance();
    YamlScanner yamlScanner = new YamlScanner();
    setPrivateFieldInTest(yamlScanner, "yamlApplicationFolderPath", "activities");
    setPrivateFieldInTest(yamlScanner, "yamlSkeletonFilePath", "src/main/resources/skeleton.yaml");
    setPrivateFieldInTest(yamlScanner, "yamlTeamsFilePath", "src/main/resources/teams.yaml");
    yamlScanner.getApplicationYamls();
    setPrivateFieldInTest(this.applicationDirector, "yamlScanner", yamlScanner);
    applicationDirector.initiateApplicationsViaCron();
  }

  private void setPrivateFieldInTest(Object targetObject, String fieldName, Object valueToSet) {
    try {
      Field field = targetObject.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(targetObject, valueToSet);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}

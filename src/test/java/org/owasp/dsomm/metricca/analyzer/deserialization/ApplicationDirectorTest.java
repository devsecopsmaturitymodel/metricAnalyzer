//package org.owasp.dsomm.metricca.analyzer.deserialization;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class ApplicationDirectorTest {
//
//  private ApplicationDirector applicationDirector;
//
//  @Mock
//  private YamlScanner yamlScanner;
//
//  @BeforeEach
//  public void setup() {
//    MockitoAnnotations.openMocks(this);
//  }
//
//  @BeforeEach
//  public void setUp() throws Exception {
//    Constructor<ApplicationDirector> constructor = ApplicationDirector.class.getDeclaredConstructor();
//    constructor.setAccessible(true);
//    applicationDirector = constructor.newInstance();
//    YamlScanner yamlScanner = new YamlScanner();
//    setPrivateField(yamlScanner, "yamlApplicationFolderPath", "src/test/resources/test-all/definitions");
//    setPrivateField(yamlScanner, "yamlSkeletonFilePath", "src/test/resources/test-all/skeleton.yaml");
//    yamlScanner.getApplicationYamls();
//    setPrivateField(this.applicationDirector, "yamlScanner", yamlScanner);
//  }
//
//  private void setPrivateField(Object targetObject, String fieldName, Object valueToSet) {
//    try {
//      Field field = targetObject.getClass().getDeclaredField(fieldName);
//      field.setAccessible(true);
//      field.set(targetObject, valueToSet);
//    } catch (NoSuchFieldException | IllegalAccessException e) {
//      throw new RuntimeException(e);
//    }
//  }
//
//  @Test
//  public void testApplicationsExists() throws Exception {
//    List<Application> actualApplications = applicationDirector.getApplications();
//    assertTrue(actualApplications.size() > 0);
//  }
//
//  @Test
//  public void testTeamActivitySecurityRequirementsExists() throws Exception {
//    List<Application> actualApplications = applicationDirector.getApplications();
//    boolean foundSecurityRequirements = false;
//    for (Application application : actualApplications) {
//      for (Activity activity : application.getActivities()) {
//        if (activity.getName().equals("Security requirements")) {
//          foundSecurityRequirements = true;
//          break;
//        }
//      }
//    }
//    assertTrue(foundSecurityRequirements);
//  }
//}

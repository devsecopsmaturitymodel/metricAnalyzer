package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.StringComponent;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ActivityTest {
  @InjectMocks
  private Activity activity;

  @Mock
  private Component component;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    activity = new Activity();
  }

  @Test
  public void testAddComponent() {
    ArrayList<String> nester = new ArrayList<>();
    nester.add("test");
    when(component.getName()).thenReturn("testComponent");
    activity.addComponent(component, nester);
    Map<String, Object> components = activity.getComponents();
    assertEquals(1, components.size());
  }

  @Test
  public void testGetName() {
    String name = "testName";
    activity.setActivityName(name);
    assertEquals(name, activity.getName());
  }

  @Test
  public void testAddContentSkeleton() {
    activity.addContentSkeleton();
    assertEquals(1, activity.getContent().size());
  }

  @Test
  public void testAddComponentWithNester() {
    ArrayList<String> nester = new ArrayList<>();
    nester.add("testNester");
    StringComponent stringComponent = new StringComponent();
    stringComponent.setName("testComponent");
    activity.addComponent(stringComponent, nester);
    Map<String, Object> components = activity.getComponents();
    assertEquals(1, components.size());
    assertTrue(components.get("testNester") instanceof Map);
  }

  @Test
  public void testAddContentSkeletonWithComponents() {
    ArrayList<String> nester = new ArrayList<>();
    StringComponent stringComponent = new StringComponent();
    stringComponent.setName("testComponent");
    activity.addComponent(stringComponent, nester);
    activity.addContentSkeleton();
    ArrayList<Map<String, Object>> content = activity.getContent();
    assertEquals(1, content.size());
    assertTrue(content.get(0).get("testComponent") instanceof StringComponent);
  }
}
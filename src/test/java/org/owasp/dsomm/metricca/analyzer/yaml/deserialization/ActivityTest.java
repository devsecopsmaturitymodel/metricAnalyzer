package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.StringComponent;

import java.util.ArrayList;
import java.util.HashMap;
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
  // Test addComponentToSkeleton method
  public void emptyNesterShouldCreateComponentDirectly() {

    // Arrange
    ArrayList<String> nester = new ArrayList<>();
    StringComponent stringComponent = new StringComponent();
    stringComponent.setName("testComponent");
    Map<String, Object> expectedSkeletonAfterAct = new HashMap<>();
    expectedSkeletonAfterAct.put("testComponent", stringComponent); // {testComponent=StringComponent[name=testComponent, value=null]};
    Map<String, Object> skeleton = activity.getSkeletons();

    // Assert
    assertEquals(0, skeleton.size());

    // Act
    activity.addComponentToSkeleton(stringComponent, nester);

    // Assert
    assertEquals(1, skeleton.size());
    assertTrue(skeleton.get("testComponent") instanceof StringComponent);
    assertEquals(expectedSkeletonAfterAct, skeleton);
  }

  @Test
  // Test addComponentToSkeleton method
  public void oneNesterWithOneComponentShouldCreateAHashMapWithSizeOne() {

    // Arrange
    ArrayList<String> nester = new ArrayList<>();
    nester.add("links");
    StringComponent stringComponent = new StringComponent();
    stringComponent.setName("title");
    Map<String, Object> excpectedInsideHashMap = new HashMap<>();
    excpectedInsideHashMap.put("title", stringComponent);
    Map<String, Object> expectedSkeletonAfterAct = new HashMap<>();
    expectedSkeletonAfterAct.put("links", excpectedInsideHashMap); // {links={title=StringComponent[name=title, value=null]}}
    Map<String, Object> skeleton = activity.getSkeletons();

    // Assert
    assertEquals(0, skeleton.size());

    // Act
    activity.addComponentToSkeleton(stringComponent, nester);

    // Assert
    assertEquals(1, skeleton.size());
    assertTrue(skeleton.get("links") instanceof Map);
    assertEquals(expectedSkeletonAfterAct, skeleton);
  }

  @Test
  // Test addComponentToSkeleton method
  public void oneNesterWithTwoComponentShouldCreateAHashMapWithSizeOne() {

    // Arrange
    ArrayList<String> nester = new ArrayList<>();
    nester.add("links");
    StringComponent stringComponent1 = new StringComponent();
    stringComponent1.setName("title");
    StringComponent stringComponent2 = new StringComponent();
    stringComponent2.setName("url");
    Map<String, Object> excpectedInsideHashMap = new HashMap<>();
    excpectedInsideHashMap.put("title", stringComponent1);
    excpectedInsideHashMap.put("url", stringComponent2);
    Map<String, Object> expectedSkeletonAfterAct = new HashMap<>();
    expectedSkeletonAfterAct.put("links", excpectedInsideHashMap); // {links={title=StringComponent[name=title, value=null], url=StringComponent[name=url, value=null]}}
    Map<String, Object> skeleton = activity.getSkeletons();

    // Assert
    assertEquals(0, skeleton.size());

    // Act
    activity.addComponentToSkeleton(stringComponent1, nester);
    activity.addComponentToSkeleton(stringComponent2, nester); // The insideHashMap should be already created

    // Assert
    assertEquals(1, skeleton.size());
    assertTrue(skeleton.get("links") instanceof Map);
    assertEquals(expectedSkeletonAfterAct, skeleton);
  }

  // TODO cloneSkeletonAndAddToContent

  // TODO getDateComponents

  // TODO getMatchingDatePeriodComponent

  // TODO getClosestBeforeDatePeriodComponent

}
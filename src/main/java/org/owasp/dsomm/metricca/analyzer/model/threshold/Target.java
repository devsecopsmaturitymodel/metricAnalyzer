package org.owasp.dsomm.metricca.analyzer.model.threshold;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.owasp.dsomm.metricca.analyzer.model.threshold.target.CountTarget;
import org.owasp.dsomm.metricca.analyzer.model.threshold.target.IntTarget;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Component;

import java.util.ArrayList;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = IntTarget.class, name = "int"),
    @JsonSubTypes.Type(value = CountTarget.class, name = "count"),
})
public abstract class Target {
  @JsonProperty("name")
  private String name;

  public Target() {
  }

  public Boolean thresholdReached(ArrayList<Component> componentArrayList) {
    return null;
  }

  protected Component getComponentForTarget(ArrayList<Component> componentArrayList) {
    for (Component component : componentArrayList) {
      if (component.getName().equals(this.name)) {
        return component;
      }
    }
    return null;
  }

  protected ArrayList<Component> getComponentsForTarget(ArrayList<Component> componentArrayList) {
    ArrayList<Component> components = new ArrayList<Component>();
    for (Component component : componentArrayList) {
      if (component.getName().equals(this.name)) {
        components.add(component);
      }
    }
    return components;
  }
}


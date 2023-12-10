package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DateComponent;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DatePeriodComponent;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.IntComponent;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.StringComponent;

import java.util.ArrayList;

public class ActivityBuilder {

  private final Activity activity;

  public ActivityBuilder() {
    this.activity = new Activity();
  }

  public ActivityBuilder setActivityName(String name) {
    activity.setActivityName(name);
    return this;
  }

  public ActivityBuilder setLevel(String level) {
    activity.setLevel(level);
    return this;
  }

  public ActivityBuilder addStringComponent(String componentName, ArrayList<String> nester) {
    StringComponent comp = new StringComponent();
    comp.setName(componentName);
    activity.addComponent(comp, nester);
    return this;
  }

  public ActivityBuilder addIntComponent(String componentName, ArrayList<String> nester) {
    IntComponent comp = new IntComponent();
    comp.setName(componentName);
    activity.addComponent(comp, nester);
    return this;
  }

  public ActivityBuilder addDateComponent(String componentName, ArrayList<String> nester) {
    DateComponent comp = new DateComponent();
    comp.setName(componentName);
    activity.addComponent(comp, nester);
    return this;
  }

  public ActivityBuilder addDatePeriodComponent(String componentName, String periodLength, ArrayList<String> nester) {
    DatePeriodComponent comp = new DatePeriodComponent(periodLength);
    comp.setName(componentName);
    activity.addComponent(comp, nester);
    return this;
  }

  public Activity build() {
    return this.activity;
  }
}

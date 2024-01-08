package org.owasp.dsomm.metricca.analyzer.deserialization;

import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Threshold;

import java.util.List;

public class ActivityBuilder {
  protected Activity activity;

  public ActivityBuilder(Activity activity) throws InstantiationException, IllegalAccessException {
    this.activity = activity;
  }

  public ActivityBuilder withName(String name) {
    activity.setName(name);
    return this;
  }

  public ActivityBuilder withThresholds(List<Threshold> thresholds) {
    activity.setThresholds(thresholds);
    return this;
  }

  public ActivityBuilder withGrafanaPanelType(String grafanaPanelType) {
    activity.setGrafanaPanelType(grafanaPanelType);
    return this;
  }

  public Activity build() {
    activity.finishActivity();
    return activity;
  }
}
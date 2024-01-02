package org.owasp.dsomm.metricca.analyzer.grafana;

import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Threshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PanelFactory {
  private static final Logger logger = LoggerFactory.getLogger(PanelFactory.class);

  public static Map<String, PanelConfiguration> getPanelsForLevels(PanelConfiguration panelConfiguration, Activity activity) {
    Map<String, PanelConfiguration> panelConfigurations = new HashMap<>();
    switch (panelConfiguration.getType()) {
      case "timeseries-flatdate":
        for (Threshold threshold : activity.getThresholds()) {
          PanelConfiguration newPanelConfiguration = new PanelConfiguration(
              panelConfiguration.getTitle() + " " + threshold.getLevel(),
              panelConfiguration.getType(),
              "activity/" + Activity.urlEncode(activity.getName()) + "/level/" + Activity.urlEncode(threshold.getLevel()) + "/flatdate");
          panelConfigurations.put(newPanelConfiguration.getTitle(), newPanelConfiguration);
        }
        break;
      case "count":
        for (Threshold threshold : activity.getThresholds()) {
          PanelConfiguration newPanelConfiguration = new PanelConfiguration(
              panelConfiguration.getTitle() + " " + threshold.getLevel(),
              panelConfiguration.getType(),
              "activity/" + Activity.urlEncode(activity.getName()) + "/level/" + Activity.urlEncode(threshold.getLevel()) + "/count");
          panelConfigurations.put(newPanelConfiguration.getTitle(), newPanelConfiguration);
        }
        break;
      default:
        panelConfigurations.put(panelConfiguration.getTitle(), panelConfiguration);
    }

    return panelConfigurations;
  }
}

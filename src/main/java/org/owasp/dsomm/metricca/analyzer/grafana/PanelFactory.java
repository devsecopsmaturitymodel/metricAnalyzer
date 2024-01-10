package org.owasp.dsomm.metricca.analyzer.grafana;

import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Threshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PanelFactory {
  private static final Logger logger = LoggerFactory.getLogger(PanelFactory.class);

  public static Map<String, PanelConfiguration> getPanelsForLevels(PanelConfiguration panelConfiguration, SkeletonActivity activity) {
    Map<String, PanelConfiguration> panelConfigurations = new HashMap<>();
    switch (panelConfiguration.getType()) {
      case "timeseries-flatdate":
        for (Threshold threshold : activity.getThresholds()) {
          PanelConfiguration newPanelConfiguration = new PanelConfiguration(
              panelConfiguration.getTitle() + " " + threshold.getLevel(),
              panelConfiguration.getType(),
              "activity/" + SkeletonActivity.urlEncode(panelConfiguration.getTitle()) + "/level/" + SkeletonActivity.urlEncode(threshold.getLevel()) + "/flatdate",
              threshold.getDescription());
          panelConfigurations.put(newPanelConfiguration.getTitle(), newPanelConfiguration);
        }
        break;
      case "count":
        for (Threshold threshold : activity.getThresholds()) {
          PanelConfiguration newPanelConfiguration = new PanelConfiguration(
              panelConfiguration.getTitle() + " " + threshold.getLevel(),
              panelConfiguration.getType(),
              "activity/" + SkeletonActivity.urlEncode(panelConfiguration.getTitle()) + "/level/" + SkeletonActivity.urlEncode(threshold.getLevel()) + "/count",
              threshold.getDescription());
          panelConfigurations.put(newPanelConfiguration.getTitle(), newPanelConfiguration);
        }
        break;
      default:
        panelConfigurations.put(panelConfiguration.getTitle(), panelConfiguration);
    }

    return panelConfigurations;
  }
}

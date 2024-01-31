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
        if (panelConfiguration.getDashboardType() == "team") {
          PanelConfiguration teamNewPanelConfiguration = new PanelConfiguration(
              getTitle(panelConfiguration, null),
              panelConfiguration.getType(),
              getURLTimeSeries(panelConfiguration, null),
              "All levels for this application");
          panelConfigurations.put(teamNewPanelConfiguration.getTitle(), teamNewPanelConfiguration);
        } else if (panelConfiguration.getDashboardType() == "overview") {
          for (Threshold threshold : activity.getThresholds()) {
            PanelConfiguration newPanelConfiguration = new PanelConfiguration(
                panelConfiguration.getTitle() + " " + threshold.getLevel(),
                panelConfiguration.getType(),
                getURLTimeSeries(panelConfiguration, threshold),
                threshold.getDescription());
            panelConfigurations.put(newPanelConfiguration.getTitle(), newPanelConfiguration);
          }
        }
        break;
      case "count":
//        if(panelConfiguration.getDashboardType() == "team") {
//          PanelConfiguration teamNewPanelConfiguration = new PanelConfiguration(
//              getTitle(panelConfiguration, null),
//              panelConfiguration.getType(),
//              getURLCount( panelConfiguration, null),
//              "All levels for this application");
//          panelConfigurations.put(teamNewPanelConfiguration.getTitle(), teamNewPanelConfiguration);
//        } else if(panelConfiguration.getDashboardType() == "overview") {
        for (Threshold threshold : activity.getThresholds()) {
          PanelConfiguration newPanelConfiguration = new PanelConfiguration(
              panelConfiguration.getTitle() + " " + threshold.getLevel(),
              panelConfiguration.getType(),
              "activity/" + SkeletonActivity.urlEncode(panelConfiguration.getTitle()) + "/level/" + SkeletonActivity.urlEncode(threshold.getLevel()) + "/count",
              threshold.getDescription());
          panelConfigurations.put(newPanelConfiguration.getTitle(), newPanelConfiguration);
        }
//        }

        break;
      default:
        panelConfigurations.put(panelConfiguration.getTitle(), panelConfiguration);
    }

    return panelConfigurations;
  }

  private static String getURLCount(PanelConfiguration panelConfiguration, Threshold threshold) {
    String url = "";
    switch (panelConfiguration.getDashboardType()) {
      case "team":
        url = "activity/" + SkeletonActivity.urlEncode(panelConfiguration.getTitle()) + "/level/" + SkeletonActivity.urlEncode(threshold.getLevel()) + "/count";
        break;
      case "overview":
        url = "activity/" + SkeletonActivity.urlEncode(panelConfiguration.getTitle()) + "/level/" + SkeletonActivity.urlEncode(threshold.getLevel()) + "/flatdate";
        break;
      default:
        logger.error("Could not find panel type for " + panelConfiguration.getDashboardType());
    }
    return url;
  }

  private static String getURLTimeSeries(PanelConfiguration panelConfiguration, Threshold threshold) {
    String url = "";
    switch (panelConfiguration.getDashboardType()) {
      case "team":
        url = "team/${team}/application/${application}/activity/" + panelConfiguration.getTitleUrlEncoded() + "/entries";
        break;
      case "overview":
        url = "activity/" + SkeletonActivity.urlEncode(panelConfiguration.getTitle()) + "/level/" + SkeletonActivity.urlEncode(threshold.getLevel()) + "/flatdate";
        break;
      default:
        logger.error("Could not find panel type for " + panelConfiguration.getDashboardType());
    }
    return url;
  }

  private static String getTitle(PanelConfiguration panelConfiguration, Threshold threshold) {
    String title = "";
    switch (panelConfiguration.getDashboardType()) {
      case "team":
        title = panelConfiguration.getTitle();
        break;
      case "overview":
        title = panelConfiguration.getTitle() + " " + threshold.getLevel();
        break;
      default:
        logger.error("Could not find panel type for " + panelConfiguration.getDashboardType());
    }
    return title;
  }
}

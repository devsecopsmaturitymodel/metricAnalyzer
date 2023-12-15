package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.grafana.OverviewDashboard;
import org.owasp.dsomm.metricca.analyzer.grafana.PanelConfiguration;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Thresholds;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.ThresholdParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class GrafanaDashboardController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaDashboardController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

  @Autowired
  private OverviewDashboard overviewDashboard;

  @RequestMapping(value = "/teamdashboard", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String getTeamDashboard() throws Exception {
    PanelConfiguration panelConfiguration = new PanelConfiguration("Team Dashboard", "timeseries", "teamdashboard");
    ArrayList<PanelConfiguration> panelConfigurations = new ArrayList<PanelConfiguration>();
    panelConfigurations.add(panelConfiguration);
    panelConfigurations.add(new PanelConfiguration("zweites Panel", "timeseries", "blabl"));

    return overviewDashboard.getDashboard(panelConfigurations);
  }

  @RequestMapping(value = "/thresholds", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public Thresholds getThresholds() throws Exception {
    Thresholds thresholds = ThresholdParser.parseYamlFile("src/main/resources/threshold.yaml");
    logger.info("Threshold: " + thresholds);

    return thresholds;
  }

}
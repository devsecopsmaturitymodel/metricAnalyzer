package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component

public class YamlToObjectManagerInit {
  private static final Logger logger = LoggerFactory.getLogger(YamlToObjectManagerInit.class);

  @Autowired
  private ApplicationDirector applicationDirector;

  @PostConstruct
  private void postConstruct() throws Exception {
    applicationDirector.getApplications();
  }
}

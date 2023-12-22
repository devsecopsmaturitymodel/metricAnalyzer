package org.owasp.dsomm.metricca.analyzer.grafana;


import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Configuration
public class OverviewDashboard extends GenericDashboard {
  protected String dashboardType = "overview";

  @Value("${metricCA.grafana.datasource.uuid:3c843a46-329c-4282-8664-5fd8174db308}")
  protected String datasourceUuid;


  public OverviewDashboard() {
    super();
  }

  @Override
  public String getDashboardType() {
    return dashboardType;
  }
}

package org.owasp.dsomm.metricca.analyzer.grafana;


import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
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
public abstract class GenericDashboard {
  private static final Logger logger = LoggerFactory.getLogger(GenericDashboard.class);
  @Value("${metricCA.api.baseurl}")
  protected String apiBaseUrl;
  @Value("${metricCA.grafana.template.path:src/main/resources/templates/}")
  protected String panelTemplatePath;
  @Value("${metricCA.grafana.template.panelbasename:panel-infinity-}")
  protected String panelBaseName;
  @Value("${metricCA.grafana.template.dashboardprefix:grafana-dashboard-}")
  protected String dashboardTemplatePrefix;
  @Value("${metricCA.grafana.template.filepostfix:.ftl}")
  protected String templateFilePostfix;
  @Value("${metricCA.grafana.datasource.uuid:e407f775-8b38-4c67-b2c3-298086473afa}")
  protected String datasourceUuid;

  public GenericDashboard() {
  }

  @Bean
  public FreeMarkerViewResolver freemarkerViewResolver() {
    FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
    resolver.setCache(false);
    resolver.setPrefix("");
    resolver.setSuffix(templateFilePostfix);
    return resolver;
  }

  @Bean
  public FreeMarkerConfigurer freemarkerConfig() {
    FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
    freeMarkerConfigurer.setTemplateLoaderPath(panelTemplatePath);
    return freeMarkerConfigurer;
  }

  public String getDashboard(Collection<PanelConfiguration> panels) throws IOException, TemplateException {
    Template template1 = getTemplate(dashboardTemplatePrefix + getDashboardType() + templateFilePostfix);
    Map<String, Object> replacements = getDashboardTemplateReplacements(panels);
    StringWriter stringWriter = new StringWriter();
    template1.process(replacements, stringWriter);
    return stringWriter.toString();
  }
  protected Map<String, Object>  getDashboardTemplateReplacements(Collection<PanelConfiguration> panels) throws TemplateException, IOException {
    Map<String, Object> replacements = new HashMap<>();
    replacements.put("title", StringUtils.capitalize(getDashboardType()));
    replacements.put("panelsAsString", String.join(",", getPanels(panels)));
    return replacements;
  }

  private Template getTemplate(String templatePath) throws IOException, TemplateException {
    FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
    freemarker.template.Configuration freeMarkerConfig = freeMarkerConfigurer.createConfiguration();
    FileTemplateLoader ftl1 = new FileTemplateLoader(new File(panelTemplatePath));
    MultiTemplateLoader mtl = new MultiTemplateLoader(new TemplateLoader[]{ftl1});

    freeMarkerConfig.setTemplateLoader(mtl);
    Template template1 = freeMarkerConfig.getTemplate(templatePath);
    return template1;
  }

  protected Collection<String> getPanels(Collection<PanelConfiguration> panelConfigurations) throws TemplateException, IOException {
    Collection<String> panels = new ArrayList<>();
    for (PanelConfiguration panelConfiguration : panelConfigurations) {
      String panel = getPanel(panelConfiguration);
      panels.add(panel);
    }
    return panels;
  }

  private String getPanel(PanelConfiguration panelConfiguration) throws TemplateException, IOException {
    Map<String, Object> input = new HashMap<>();
    input.put("title", panelConfiguration.getTitle());
    input.put("apiUrl", apiBaseUrl + "/" + panelConfiguration.getUrl());
    input.put("datasource-uuid", datasourceUuid);

    Template template = getTemplate(panelBaseName + getDashboardType() + "/" + panelConfiguration.getType() + templateFilePostfix);
    StringWriter stringWriter = new StringWriter();
    template.process(input, stringWriter);
    return stringWriter.toString();
  }

  private String readFile(File file) {
    try {
      Scanner myReader = new Scanner(file);
      String data = "";
      while (myReader.hasNextLine()) {
        data = data + myReader.nextLine();
      }
      myReader.close();
      return data;
    } catch (FileNotFoundException e) {
      logger.error("An error occurred while reading file.");
      e.printStackTrace();
    }
    return null;
  }

  public abstract String getDashboardType();
}

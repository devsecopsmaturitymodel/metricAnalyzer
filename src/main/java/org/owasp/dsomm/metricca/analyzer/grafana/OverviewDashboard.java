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
public class OverviewDashboard {
  private static final Logger logger = LoggerFactory.getLogger(OverviewDashboard.class);


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

  @Value("${metricCA.api.baseurl}")
  private String apiBaseUrl;

  @Value("${metricCA.grafana.template.path:src/main/resources/templates/}")
  private String panelTemplatePath;
  @Value("${metricCA.grafana.template.panelbasename:panel-infinity-}")
  private String panelBaseName;

  @Value("${metricCA.grafana.template.dashboardprefix:grafana-dashboard-}")
  private String dashboardTemplatePrefix;

  @Value("${metricCA.grafana.template.filepostfix:.ftl}")
  private String templateFilePostfix;

  @Value("${metricCA.grafana.datasource.uuid:overview:e407f775-8b38-4c67-b2c3-298086473afa}")
  private String datasourceUuid;

  private String dashboardType = "overview";
  public OverviewDashboard() {
  }

  public String getDashboard(Collection<PanelConfiguration> panels) throws IOException, TemplateException {
    Template template1 = getTemplate(dashboardTemplatePrefix + dashboardType + templateFilePostfix);
    Map<String, Object> input = new HashMap<>();
    input.put("title", "Vogella example");
    input.put("panelsAsString",  String.join(",", getPanels(panels)));
    StringWriter stringWriter = new StringWriter();
    template1.process(input, stringWriter);
    return stringWriter.toString();
  }

  private Template getTemplate(String templatePath) throws IOException, TemplateException {
    FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
    freemarker.template.Configuration freeMarkerConfig = freeMarkerConfigurer.createConfiguration();
    FileTemplateLoader ftl1 = new FileTemplateLoader(new File(panelTemplatePath));
    MultiTemplateLoader mtl = new MultiTemplateLoader(new TemplateLoader[] { ftl1 });

    freeMarkerConfig.setTemplateLoader(mtl);
    Template template1 =  freeMarkerConfig.getTemplate(templatePath);
    return template1;
  }

  private Collection<String> getPanels(Collection<PanelConfiguration> panelConfigurations) throws TemplateException, IOException {
    Collection<String> panels = new ArrayList<>();
    for (PanelConfiguration panelConfiguration : panelConfigurations) {
      String panel = getPanel(panelConfiguration);
      panels.add( panel);
    }
  return panels;
  }
  private String getPanel(PanelConfiguration panelConfiguration) throws TemplateException, IOException {
    Template template = getTemplate(panelBaseName +dashboardType+"/" +  panelConfiguration.getType() + templateFilePostfix);
    Map<String, Object> input = new HashMap<>();
    input.put("title", panelConfiguration.getTitle());
    input.put("apiUrl", apiBaseUrl + "/"+panelConfiguration.getUrl());
    input.put("datasource-uuid", datasourceUuid);

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
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return null;
  }
  public String getDashboardType() {
    return dashboardType;
  }

  public void setDashboardType(String dashboardType) {
    this.dashboardType = dashboardType;
  }
}

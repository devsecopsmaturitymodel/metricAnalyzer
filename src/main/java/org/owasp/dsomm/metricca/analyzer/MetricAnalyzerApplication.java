package org.owasp.dsomm.metricca.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;

@EnableConfigurationProperties
@SpringBootApplication
//@ComponentScan(basePackages = {"org.owasp.dsomm.metricca.analyzer"})
public class MetricAnalyzerApplication {
  private static final Logger logger = LoggerFactory.getLogger(MetricAnalyzerApplication.class);

  public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    for (String arg : args) {
      logger.info("arg: " + arg);
    }

    SpringApplication.run(MetricAnalyzerApplication.class, args);
  }
}

package org.owasp.dsomm.metricCA.metricAnalyzer;

import org.owasp.dsomm.metricCA.analyzer.controller.GrafanaController;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.YamlToObjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan(basePackageClasses = GrafanaController.class)
public class MetricAnalyzerApplication {
	private static final Logger logger = LoggerFactory.getLogger(MetricAnalyzerApplication.class);


	public static void main(String[] args) {
		for(String arg: args){
			logger.info("arg: " + arg);
		}
		YamlToObjectManager.getActivities(); // Test it works
		SpringApplication.run(MetricAnalyzerApplication.class, args);
	}
}

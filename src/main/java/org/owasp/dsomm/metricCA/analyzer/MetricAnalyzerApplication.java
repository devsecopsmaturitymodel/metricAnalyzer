package org.owasp.dsomm.metricCA.analyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.owasp.dsomm.metricCA.analyzer.controller.GrafanaController;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.YamlToObjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = GrafanaController.class)
public class MetricAnalyzerApplication {
	private static final Logger logger = LoggerFactory.getLogger(MetricAnalyzerApplication.class);


	public static void main(String[] args) {
		for(String arg: args){
			logger.info("arg: " + arg);
		}
		ObjectMapper mapper = JsonMapper.builder().addModule(new JodaModule()).build();

		YamlToObjectManager.getApplications(); // Test it works
		SpringApplication.run(MetricAnalyzerApplication.class, args);
	}
}

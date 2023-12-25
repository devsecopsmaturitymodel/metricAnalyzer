package org.owasp.dsomm.metricCA.analyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.owasp.dsomm.metricCA.analyzer.controller.GrafanaController;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.YamlToObjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileNotFoundException;
@EnableConfigurationProperties
@SpringBootApplication
//@ComponentScan(basePackages = {"org.owasp.dsomm.metricCA.analyzer"})
public class MetricAnalyzerApplication {
	private static final Logger logger = LoggerFactory.getLogger(MetricAnalyzerApplication.class);

	public static void main(String[] args) throws FileNotFoundException {
		for(String arg: args){
			logger.info("arg: " + arg);
		}

		SpringApplication.run(MetricAnalyzerApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().registerModule(new JodaModule());
	}

}

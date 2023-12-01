package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ConfigurationPath {
    @Value("${metricCA.configuration.yaml-path:/home/tpagel/git/metricAnalyzer/definitions/configuration.yaml}")
    public String yamlConfigurationFilePath;

    @Value("${metricCA.application.yaml-path:/home/tpagel/git/metricAnalyzer/definitions/App1.yaml}")
    public String yamlApplicationFilePath;
}

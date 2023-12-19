package org.owasp.dsomm.metricCA.analyzer.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class TokenConfiguration {

  public static final String METRIC_CA_ACCESS_TOKEN_SECRET = "metricCA.access.token.secret";

  @Value("${metricCA.access.token.secret.path}")
  private String secretFilePath;

  public String getToken() {
    Resource resource = new ClassPathResource(secretFilePath);
    try {
      Properties properties = PropertiesLoaderUtils.loadProperties(resource);
      return properties.get(METRIC_CA_ACCESS_TOKEN_SECRET).toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}

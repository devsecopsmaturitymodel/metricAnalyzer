package org.owasp.dsomm.metricCA.analyzer.security.config;

import org.apache.logging.log4j.util.Strings;
import org.owasp.dsomm.metricCA.analyzer.exception.SkeletonNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Configuration
public class TokenConfiguration {

  public static final String METRIC_CA_ACCESS_TOKEN_SECRET = "metricCA.access.token.secret";
  public static final String TOKEN_NOT_FOUND = "Token must not be null";
  public static final String SECRET_FILE_PATH_NOT_FOUND = "secretFilePath must not be null";

  @Value("${metricCA.access.token.secret.path}")
  private String secretFilePath;
  @Value("${metricCA.access.token.enabled}")
  private boolean enabled;

  private String token;

  @PostConstruct
  public void builtToken() {
    Resource resource = new ClassPathResource(secretFilePath);
    try {
      if (enabled) {
        if (Strings.isNotEmpty(secretFilePath)) {
          Properties properties = PropertiesLoaderUtils.loadProperties(resource);
          String tokenValue = properties.get(METRIC_CA_ACCESS_TOKEN_SECRET).toString();
          if (Strings.isNotEmpty(tokenValue)) {
            token = tokenValue;
          } else {
            throw new SkeletonNotFoundException(TOKEN_NOT_FOUND);
          }
        } else {
          throw new SkeletonNotFoundException(SECRET_FILE_PATH_NOT_FOUND);
        }
      }
    } catch (FileNotFoundException cause) {
      throw new SkeletonNotFoundException(cause.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getToken(){
    return token;
  }

}

package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Thresholds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ThresholdParser {

  private static final Logger logger = LoggerFactory.getLogger(ThresholdParser.class);

  public static Thresholds parseYamlFile(String filePath) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.enableDefaultTyping();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

    return mapper.readValue(new File(filePath), Thresholds.class);
  }

  public static Thresholds parseYaml(String yaml) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.enableDefaultTyping();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    logger.debug("YAML: " + yaml);

    return mapper.readValue(yaml, Thresholds.class);
  }
}

package org.owasp.dsomm.metricca.analyzer.deserialization;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class YamlScannerIncludesTest {

  @Test
  void testSingleNodeInclude() throws Exception {
    // Arrange
    String basePath = Paths.get("src", "test", "resources", "include").toString();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode applicationJsonNode = objectMapper.readTree("{ \"key\": \"value\", \"includes\": [\"include1.yaml\"] }");
    JsonNode expected = objectMapper.readTree("{ \"key\": \"value\", \"includedKey\": \"includedValue\" }");

    // Act
    YamlScannerIncludes yamlScannerIncludes = new YamlScannerIncludes(applicationJsonNode, basePath);
    JsonNode result = yamlScannerIncludes.getNode();

    // Assert
    assertEquals(expected, result);
  }

  @Test
  void testMultipleNodeInclude() throws Exception {
    // Arrange
    String basePath = Paths.get("src", "test", "resources", "include").toString();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode applicationJsonNode = objectMapper.readTree("{ \"key\": \"value\", \"includes\": [\"include2.yaml\"] }");
    JsonNode expected = objectMapper.readTree("{ \"key\": \"value\", \"includedKey2\": \"includedValue2\" }");

    // Act
    YamlScannerIncludes yamlScannerIncludes = new YamlScannerIncludes(applicationJsonNode, basePath);
    JsonNode result = yamlScannerIncludes.getNode();

    // Assert
    assertEquals(expected, result);
  }

  @Test
  void testNestedIncludes() throws Exception {
    // Arrange
    String basePath = Paths.get("src", "test", "resources", "include").toString();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode applicationJsonNode = objectMapper.readTree("{ \"key\": \"value\", \"includes\": [\"nestedInclude.yaml\"] }");
    JsonNode expected = objectMapper.readTree("{ \"key\": \"value\", \"includedKey1\": \"includedValue1\", \"nestedKey2\": \"nestedValue2\"}");
    // Act
    YamlScannerIncludes yamlScannerIncludes = new YamlScannerIncludes(applicationJsonNode, basePath);
    JsonNode result = yamlScannerIncludes.getNode();

    // Assert
    assertEquals(expected, result);
  }
  @Test
  void testIncludesWithActivities() throws Exception {
    // Arrange
    String basePath = Paths.get("src", "test", "resources", "include").toString();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode applicationJsonNode = objectMapper.readTree("{ \"activities\": {\"key\": \"value\"}, \"includes\": [\"includeWithActivities.yaml\"]}");
    JsonNode expected = objectMapper.readTree("{ \"activities\": {\"key\": \"value\", \"includedKey\": \"includedValue\"}}");
    // Act
    YamlScannerIncludes yamlScannerIncludes = new YamlScannerIncludes(applicationJsonNode, basePath);
    JsonNode result = yamlScannerIncludes.getNode();

    // Assert
    assertEquals(expected, result);
  }
}

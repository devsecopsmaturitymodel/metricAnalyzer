package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class YamlScannerIncludes {
  private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
  private String yamlBasePath;
  private JsonNode applicationJsonNode;

  public YamlScannerIncludes(JsonNode applicationJsonNode, String basePath) {
    this.applicationJsonNode = applicationJsonNode;
    this.yamlBasePath = basePath;
  }

  public JsonNode getNode() throws IOException {
    processIncludes(applicationJsonNode);
    return applicationJsonNode;
  }

  private void processIncludes(JsonNode node) throws IOException {
    if (node.has("includes")) {
      ArrayNode includesArray = (ArrayNode) node.get("includes");
      for (JsonNode includeNode : includesArray) {
        if (includeNode.isTextual()) {
          String includePath = includeNode.asText();
          File includeFile = new File(yamlBasePath + File.separator + includePath);
          JsonNode includeContent = mapper.readTree(includeFile);
          if (includeContent.has("includes")) {
              processIncludes(includeContent);
          }
          mergeJsonNodes(node, includeContent);
        }
      }
      ((ObjectNode) node).remove("includes");
    }


  }

  private void mergeJsonNodes(JsonNode destinationNode, JsonNode sourceNode) {
    if (destinationNode instanceof ObjectNode && sourceNode instanceof ObjectNode) {
      ObjectNode destObjectNode = (ObjectNode) destinationNode;
      ObjectNode srcObjectNode = (ObjectNode) sourceNode;

      // Merge fields from source node to destination node
      srcObjectNode.fieldNames().forEachRemaining(fieldName -> {
        JsonNode srcFieldValue = srcObjectNode.get(fieldName);
        JsonNode destFieldValue = destObjectNode.get(fieldName);

        if (destFieldValue != null && destFieldValue.isObject() && srcFieldValue.isObject()) {
          // Recursive deep merge for nested objects
          mergeJsonNodes(destFieldValue, srcFieldValue);
        } else {
          // If the field exists in the destination node but not in the source node, or if the field is null in the destination
          // and exists in the source node, or if the included YAML is overriding the including YAML, override the value in the destination
          if (destFieldValue == null || (!destObjectNode.has(fieldName) && srcObjectNode.has(fieldName))) {
            destObjectNode.set(fieldName, srcFieldValue);
          }
        }
      });
    } else {
      throw new IllegalArgumentException("Both nodes must be ObjectNodes");
    }
  }

}

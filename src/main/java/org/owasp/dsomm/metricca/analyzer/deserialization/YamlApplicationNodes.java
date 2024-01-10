package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class YamlApplicationNodes {
  private static final Logger logger = LoggerFactory.getLogger(YamlApplicationNodes.class);
  // Key: Team name
  protected HashMap<String, ArrayList<JsonNode>> nodes;

  public YamlApplicationNodes() {
    this.nodes = new HashMap<String, ArrayList<JsonNode>>();
  }

  public void addJsonNode(JsonNode node) {
    JsonNode settings = node.get("settings");
    String team = settings.get("team").asText();
    if (this.nodes.containsKey(team)) {
      this.nodes.get(team).add(node);
    } else {
      ArrayList<JsonNode> list = new ArrayList<JsonNode>();
      list.add(node);
      this.nodes.put(team, list);
    }
  }

  public HashMap<String, ArrayList<JsonNode>> getNodes() {
    return nodes;
  }

  public ArrayList<JsonNode> getNodesForTeam(String team) {
    ArrayList<JsonNode> allNodes = new ArrayList<JsonNode>();

    for (JsonNode node : nodes.get(team)) {
      JsonNode settings = node.get("settings");
      String teamFromNode = settings.get("team").asText();
      if (teamFromNode.equals(team)) {
        allNodes.add(node);
      }
    }
    return allNodes;
  }

  public ArrayList<JsonNode> getNodes(String team, String kind) {
    ArrayList<JsonNode> applicationNodes = new ArrayList<JsonNode>();

    for (JsonNode node : getNodesForTeam(team)) {
      if (node.get("kind").asText().equals(kind)) {
        applicationNodes.add(node);
      }
    }
    return applicationNodes;
  }
}

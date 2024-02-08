package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class YamlApplicationNodesTest {

  private static final String TEAM = "team";
  private static final String SETTINGS = "settings";
  private static final String KIND = "kind";
  private static final String jsonNodeString = """ 
                  {"settings":{"team":"team"},"kind":"kind"}
          """;

  ObjectMapper objectMapper = new ObjectMapper();

  @Mock
  private JsonNode jsonNode;
  @InjectMocks
  private YamlApplicationNodes yamlApplicationNodes;

  @Test
  void shouldAddJsonNode() {
    when(jsonNode.get(TEAM)).thenReturn(jsonNode);
    when(jsonNode.get(TEAM).asText()).thenReturn(TEAM);

    when(jsonNode.get(SETTINGS)).thenReturn(jsonNode);

    yamlApplicationNodes.addJsonNode(jsonNode);

    assertThat(yamlApplicationNodes.nodes)
            .containsKey(TEAM)
            .extracting(TEAM)
            .satisfies(nodeList -> {
              assertThat((ArrayList<JsonNode>) nodeList).hasSize(1);
              assertThat((ArrayList<JsonNode>) nodeList).contains(jsonNode);
            });
  }

  @Test
  void shouldAddJsonNodeForIf() throws JsonProcessingException {
    HashMap<String, ArrayList<JsonNode>> nodes = new HashMap<>();
    ArrayList<JsonNode> jsonNodes = new ArrayList<>();
    jsonNodes.add(objectMapper.readTree(jsonNodeString));
    nodes.put("team", jsonNodes);

    ReflectionTestUtils.setField(yamlApplicationNodes, "nodes", nodes);

    yamlApplicationNodes.addJsonNode(objectMapper.readTree(jsonNodeString));

    assertThat(yamlApplicationNodes.nodes).isNotNull();
  }

  @Test
  public void shouldGetNodesTest() {
    when(jsonNode.get(TEAM)).thenReturn(jsonNode);
    when(jsonNode.get(TEAM).asText()).thenReturn(TEAM);
    when(jsonNode.get(SETTINGS)).thenReturn(jsonNode);

    yamlApplicationNodes.addJsonNode(jsonNode);

    assertThat(yamlApplicationNodes.getNodes()).isNotEmpty().hasSize(1);
  }

  @Test
  void shouldGetNodesForTeam() {
    when(jsonNode.get(TEAM)).thenReturn(jsonNode);
    when(jsonNode.get(TEAM).asText()).thenReturn(TEAM);
    when(jsonNode.get(SETTINGS)).thenReturn(jsonNode);

    yamlApplicationNodes.addJsonNode(jsonNode);

    assertThat(yamlApplicationNodes.getNodesForTeam(TEAM))
            .hasSize(1)
            .contains(jsonNode);
  }

  @Test
  void testGetNodesForTeam() throws JsonProcessingException {
    HashMap<String, ArrayList<JsonNode>> nodes = new HashMap<>();
    ArrayList<JsonNode> jsonNodes = new ArrayList<>();
    jsonNodes.add(objectMapper.readTree(jsonNodeString));
    nodes.put("team", jsonNodes);

    ReflectionTestUtils.setField(yamlApplicationNodes, "nodes", nodes);

    ArrayList<JsonNode> nodesForTeam = yamlApplicationNodes.getNodes(TEAM, KIND);

    assertThat(nodesForTeam)
            .hasSize(1)
            .asList()
            .extracting(JsonNode.class::cast)
            .first()
            .returns(jsonNodeString.trim(), jsonNode1 -> jsonNode1.toString());
  }

}

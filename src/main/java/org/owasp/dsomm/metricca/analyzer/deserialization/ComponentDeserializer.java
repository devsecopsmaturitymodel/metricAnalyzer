package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.DateLinkTitle;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ComponentDeserializer extends JsonDeserializer<Date> {
  private static final Logger logger = LoggerFactory.getLogger(ComponentDeserializer.class);

  @Override
  public Date deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    logger.error("########in deserializing");
    // Beispiellogik zur Bestimmung der zu instanziierenden Subklasse
    if (node.has("dateLinkTitles")) {
      logger.error("deserializing dateLinkTitle");
      return jp.getCodec().treeToValue(node, DateLinkTitle.class);
    }  else if (node.has("urls")) {
      return jp.getCodec().treeToValue(node, Url.class);
    }

    // Standard-Verhalten oder Fehler werfen, wenn keine passende Subklasse gefunden wird
    throw new RuntimeException("Unknown component type");
  }
}

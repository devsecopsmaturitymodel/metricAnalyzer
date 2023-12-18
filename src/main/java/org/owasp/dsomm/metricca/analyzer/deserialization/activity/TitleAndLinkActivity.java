package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Link;

import java.util.List;

public class TitleAndLinkActivity extends Activity {
  @JsonProperty("title")
  private String title;

  @JsonProperty("links")
  private List<Link> links;
}

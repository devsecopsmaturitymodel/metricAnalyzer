package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;

import java.net.URL;
import java.util.List;

public class UrlActivity extends Activity {
  @JsonProperty("urls")
  protected List<URL> urls;


  public List<URL> getUrls() {
    return urls;
  }

  public void setUrls(List<URL> urls) {
    this.urls = urls;
  }
}

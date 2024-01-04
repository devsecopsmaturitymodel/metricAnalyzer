package org.owasp.dsomm.metricca.analyzer.grafana;

import org.owasp.dsomm.metricca.analyzer.MetricAnalyzerApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.time.Duration;

public class GrafanaDashboardCreator {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaDashboardCreator.class);

  private final String grafanaUrl;
  private final String apiKey;
  private final String jsonContent;

  private int connectTimeout = 10;
  public GrafanaDashboardCreator(String grafanaUrl, String apiKey, String jsonContent) {
    this.grafanaUrl = grafanaUrl;
    this.apiKey = apiKey;
    this.jsonContent = jsonContent;
  }
  public GrafanaDashboardCreator(String grafanaUrl, String apiKey, String jsonContent, int connectTimeout) {
    this.grafanaUrl = grafanaUrl;
    this.apiKey = apiKey;
    this.jsonContent = jsonContent;
    this.connectTimeout = connectTimeout;
  }
  public boolean pushDashboard() throws Exception {
    String grafanaUrl = this.grafanaUrl + "/api/dashboards/db";
    String jsonContentWrapper = "{ \"dashboard\": "+jsonContent+", \"overwrite\": true }";

    HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(connectTimeout)).build();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(grafanaUrl))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + apiKey)
        .POST(HttpRequest.BodyPublishers.ofString(jsonContentWrapper))
        .build();
    System.out.println("URI: " + request.uri());
    System.out.println("Method: " + request.method());
    System.out.println("Headers: " + request.headers().map());
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      logger.info("apiKey: " + apiKey);
      logger.info("grafanaUrl: " + grafanaUrl);
      logger.error("Status Code: " + response.statusCode());
      logger.error("Response Body: " + response.body());
      return false;
    } else {
      logger.debug("Status Code: " + response.statusCode());
      logger.debug("Response Body: " + response.body());
      return true;
    }
  }
}
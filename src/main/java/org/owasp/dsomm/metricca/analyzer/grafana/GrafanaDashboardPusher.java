package org.owasp.dsomm.metricca.analyzer.grafana;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class GrafanaDashboardPusher {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaDashboardCreator.class);

  private final String grafanaUrl;
  private final String apiKey;
  private final String jsonContent;

  private int connectTimeout = 10;


  public GrafanaDashboardPusher(String grafanaUrl, String apiKey, String jsonContent, int connectTimeout) {
    this.grafanaUrl = grafanaUrl;
    this.apiKey = apiKey;
    this.jsonContent = jsonContent;
    this.connectTimeout = connectTimeout;
  }

  public boolean pushDashboard() throws Exception {
    String grafanaUrl = this.grafanaUrl + "/api/dashboards/db";
    String jsonContentWrapper = "{ \"dashboard\": " + jsonContent + ", \"overwrite\": true }";

    HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(connectTimeout)).build();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(grafanaUrl))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + apiKey)
        .POST(HttpRequest.BodyPublishers.ofString(jsonContentWrapper))
        .build();
    logger.info("URI: " + request.uri() + " - method " + request.method());
    logger.debug("Headers: " + request.headers().map());
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      logger.debug("apiKey: " + apiKey);
      logger.info("grafanaUrl: " + grafanaUrl);
      logger.error("Status Code: " + response.statusCode() + " - Response Body" + response.body());
      return false;
    } else {
      logger.debug("Status Code: " + response.statusCode() + " - Response Body" + response.body());
      return true;
    }
  }
}

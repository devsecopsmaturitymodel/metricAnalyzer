package org.owasp.dsomm.metricca.analyzer.exception;

public class ApplicationNotFoundException extends RuntimeException {
  public ApplicationNotFoundException(String applicationName, String teamName) {
    super("Application " + applicationName + " for team "+teamName+" not found");
    this.applicationName = applicationName;
    this.teamName = teamName;
  }

  private final String teamName;
  private String applicationName;

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }
}

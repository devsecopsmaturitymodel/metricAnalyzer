package org.owasp.dsomm.metricCA.analyzer.exception;

public class ComponentNotFoundException extends RuntimeException {
  public ComponentNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}

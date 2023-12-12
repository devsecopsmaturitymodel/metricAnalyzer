package org.owasp.dsomm.metricca.analyzer.exception;

public class ComponentNotFoundException extends RuntimeException {
  public ComponentNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}

package org.owasp.dsomm.metricca.analyzer.exception;

public class SkeletonNotFoundException extends RuntimeException {
  public SkeletonNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}

package org.owasp.dsomm.metricCA.analyzer.exception;

public class SkeletonNotFoundException extends RuntimeException {
    public SkeletonNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

package org.owasp.dsomm.metricca.analyzer.model.threshold;

class TargetValue {
    private Integer minValue;
    private Integer maxValue;
    private String periodType; // "calendar year" oder "sliding"
    private String period; // z.B. "1y"
    // Getter und Setter ...
}

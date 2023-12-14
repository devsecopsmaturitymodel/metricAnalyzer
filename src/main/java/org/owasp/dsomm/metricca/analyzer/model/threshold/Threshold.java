package org.owasp.dsomm.metricca.analyzer.model.threshold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Threshold {
    String level;
    Types type;
    enum Types {
        activity,
        component,
    }
    private Boolean isThresholdReached = false;
    private List<Target> targets;

}

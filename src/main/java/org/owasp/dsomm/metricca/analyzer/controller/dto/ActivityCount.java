package org.owasp.dsomm.metricca.analyzer.controller.dto;

import org.owasp.dsomm.metricca.analyzer.model.Activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ActivityCount {
  private final Map<String, Integer> entries = new HashMap<>();

  ActivityCount(Collection<Activity> activities) {
    for (Activity activity : activities) {
      if (entries.containsKey(activity.getName())) {
        entries.put(activity.getName(), entries.get(activity.getName()) + 1);
      } else {
        entries.put(activity.getName(), 1);
      }
    }
  }
}

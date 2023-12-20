package org.owasp.dsomm.metricca.analyzer.controller.dto;

import org.owasp.dsomm.metricca.analyzer.model.Activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ActivityCount {
  private Map<String, Integer> entries;

  ActivityCount(Collection<Activity> activities) {
    entries = new HashMap<>();
    for (Activity activity : activities) {
      if (entries.containsKey(activity.getName())) {
        entries.put(activity.getName(), entries.get(activity.getName()) + 1);
      } else {
        entries.put(activity.getName(), 1);
      }
    }
  }
}

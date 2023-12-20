package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.ThresholdDatePeriod;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Target;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Threshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public abstract  class Activity {
  private static final Logger logger = LoggerFactory.getLogger(Activity.class);

  @JsonProperty("name")
  protected String name;

  @JsonProperty("thresholds")
  protected List<Threshold> thresholds = new ArrayList<Threshold>();

  public List<Threshold> getThresholds() {
    return thresholds;
  }

  public void setThresholds(List<Threshold> thresholds) {
    this.thresholds = thresholds;
  }

  public void generateThresholds()  {
//    for(Threshold threshold : thresholds) {
//      Boolean thresholdReached = true;
//      for (Target target : threshold.getTargets()) {
//        target.setThresholdValue(getThresholdValue());
//      }
//    }
  }

  protected Threshold getThresholdForLevel(String level) {
    for(Threshold threshold : thresholds) {
      if(threshold.getLevel().equals(level)) {
        return threshold;
      }
    }
    return null;
  }

  @JsonProperty("is activity implemented")
  public Map<String, Boolean> isActivityImplemented() {
    Map<String, Boolean> thresholdsReached = new HashMap<>();
    for(Threshold threshold : thresholds) {
      Boolean thresholdReached = true;
      for (Target target : threshold.getTargets()) {
        if(!target.thresholdReached()) {
          thresholdReached = false;
        }
      }
      thresholdsReached.put(threshold.getLevel(), thresholdReached);
    }

    return thresholdsReached;
  }
//        if (containsDate) {
//          for (Object component : componentMap.values()) {
//            if (component instanceof DatePeriodComponent && !(component instanceof DatePeriodEndComponent)) {
//              DatePeriodEndComponent end = new DatePeriodEndComponent(((DatePeriodComponent) component).getPeriod());
//              end.setName(((DatePeriodComponent) component).getName());
//              end.setValue(((DatePeriodComponent) component).getValue());
//              HashMap<String, Object> content = new HashMap<String, Object>();
//              content.put(((DatePeriodComponent) component).getName(), end);
//              if (isPeriodBetweenTwoDates(activity, i, end.getValue())) {
//                logger.debug("Found date which is between two dates in period, not adding: " + activity.getName() + " in application: " + getApplication() + " with datePeriodComponent: " + component + " team: " + getTeam() + " and value: " + ((DatePeriodComponent) component).isActive());
//                continue;
//              }
//              newContent.add(content);
//            }
//          }

//  private boolean isDateBetweenTwoDates(Date date, int i, DatePeriodActivity activity) {
//    List<DatePeriod> datePeriods = activity.getDateComponents();
//    for (;i< datePeriods.size();i++ ) {
//      DatePeriod datePeriod = datePeriods.get(i);
//      if(datePeriod.isInPeriod(date)) {
//        return true;
//      }
//    }
//    return false;
//  }

  //  @JsonIgnore
//  public void setDatePeriod() {
//    for (Threshold threshold : this.getThresholds()) {
//      if (threshold.getDatePeriod() != null) {
//        for (DatePeriod component : getDateComponents()) {
//          component.setPeriod(threshold.getDatePeriod().getPeriodAsPeriod());
//        }
//      }
//    }
//  }
//  public List<DatePeriod> getDateComponentsInPeriod() {
//    List<DatePeriod> datePeriods = getDateComponents();
//    for(DatePeriod component : getDateComponents()) {
//      if(component.)) {
//        getDateComponents().remove(component);
//      }
//    }
//
//  }
  protected HashMap<String, ThresholdDatePeriod> thresholdDatePeriodMap;
  public void generateComponentDatePeriodFromThresholds() {
    thresholdDatePeriodMap = new HashMap<String, ThresholdDatePeriod>();
    for (Threshold threshold : this.getThresholds()) {
      thresholdDatePeriodMap.put(threshold.getLevel(), new ThresholdDatePeriod(threshold, getDateComponents()));
    }
  }


  public void finishActivity() {
    generateComponentDatePeriodFromThresholds();
    generateThresholds();
    for(ThresholdDatePeriod thresholdDatePeriod : thresholdDatePeriodMap.values()) {
      thresholdDatePeriod.setEndDatesBetweenPeriodAsInvisible();
    }
  }
  public List<DatePeriod> getDateComponents(String level) {
    if(thresholdDatePeriodMap == null) {
      return null;
    }
    return thresholdDatePeriodMap.get(level).getThresholdDatePeriods();
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public HashMap<String, ThresholdDatePeriod> getThresholdDatePeriodMap() {
    return thresholdDatePeriodMap;
  }

  public void setThresholdDatePeriodMap(HashMap<String, ThresholdDatePeriod> thresholdDatePeriodMap) {
    this.thresholdDatePeriodMap = thresholdDatePeriodMap;
  }
  public abstract List<Date> getDateComponents();
}

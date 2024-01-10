package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.ThresholdDatePeriodManager;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Target;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Threshold;
import org.owasp.dsomm.metricca.analyzer.grafana.PanelConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Activity {
  private static final Logger logger = LoggerFactory.getLogger(Activity.class);

  @JsonProperty("name")
  protected String name;

  @JsonProperty("thresholds")
  protected List<Threshold> thresholds = new ArrayList<Threshold>();

  @JsonProperty("kind")
  protected String kind;

  @JsonProperty("grafana panel type")
  protected String grafanaPanelType;
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
  protected HashMap<String, ThresholdDatePeriodManager> thresholdDatePeriodMap;

  public static String urlEncode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
  }

  public List<Threshold> getThresholds() {
    return thresholds;
  }

  public void setThresholds(List<Threshold> thresholds) {
    this.thresholds = thresholds;
  }

  public void generateThresholds() {
    for (Threshold threshold : thresholds) {
      for (Target target : threshold.getTargets()) {
        target.setThresholdValue(thresholdDatePeriodMap.get(threshold.getLevel()).getThresholdValue());
      }
    }
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

  protected Threshold getThresholdForLevel(String level) {
    for (Threshold threshold : thresholds) {
      if (threshold.getLevel().equals(level)) {
        return threshold;
      }
    }
    return null;
  }

  @JsonProperty("is activity implemented")
  public Map<String, Boolean> isActivityImplemented() {
    Map<String, Boolean> isImplementedMap = new HashMap<>();
    for (Threshold threshold : thresholds) {
      Boolean isImplemented = true;
      for (Target target : threshold.getTargets()) {
        if (target == null || target.implemented() == null || !target.implemented()) {
          logger.info("target.implemented() == null" + (target.implemented() == null));
          isImplemented = false;
        }
      }
      isImplementedMap.put(threshold.getLevel(), isImplemented);
    }

    return isImplementedMap;
  }

  public void generateComponentDatePeriodFromThresholds() {
    thresholdDatePeriodMap = new HashMap<String, ThresholdDatePeriodManager>();
    for (Threshold threshold : this.getThresholds()) {
      thresholdDatePeriodMap.put(threshold.getLevel(), new ThresholdDatePeriodManager(threshold, getDateComponents()));
    }
  }

  public void finishActivity() {
    generateComponentDatePeriodFromThresholds();
    generateThresholds();
    setEndDatesBetweenPeriodAsInvisible();
    setTargetThresholdValue();
  }

  protected void setTargetThresholdValue() {
    for (Threshold threshold : thresholds) {
      Integer count = thresholdDatePeriodMap.get(threshold.getLevel()).getThresholdValue();
      for (Target target : threshold.getTargets()) {
        target.setThresholdValue(count);
      }
    }
  }

  private void setEndDatesBetweenPeriodAsInvisible() {
    for (Threshold threshold : thresholds) {
      thresholdDatePeriodMap.get(threshold.getLevel()).setEndDatesBetweenPeriodAsInvisible();
    }
  }

  public List<DatePeriod> getDateComponents(String level) {
    if (thresholdDatePeriodMap == null) {
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

  public HashMap<String, ThresholdDatePeriodManager> getThresholdDatePeriodMap() {
    return thresholdDatePeriodMap;
  }

  public void setThresholdDatePeriodMap(HashMap<String, ThresholdDatePeriodManager> thresholdDatePeriodMap) {
    this.thresholdDatePeriodMap = thresholdDatePeriodMap;
  }

  public abstract List<Date> getDateComponents();

  public PanelConfiguration getPanelConfiguration() {
    return new PanelConfiguration(name, grafanaPanelType, "activity/" + urlEncode(name), "");
  }

  public Date getMatchingDatePeriodComponent(java.util.Date givenDate) {
    for (Date dateComponent : this.getDateComponents()) {
      if (dateComponent.getDate().equals(givenDate)) {
        return dateComponent;
      }
    }
    return null;
  }

  public String getGrafanaPanelType() {
    return grafanaPanelType;
  }

  public void setGrafanaPanelType(String grafanaPanelType) {
    this.grafanaPanelType = grafanaPanelType;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }
}

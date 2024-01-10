package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.DatePeriodHoursAndPeople;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.ThresholdDatePeriodManager;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.target.CountHoursAndPeopleTarget;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Target;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Threshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class SecurityTrainingActivity extends DatePeriodActivity {
  private static final Logger logger = LoggerFactory.getLogger(Activity.class);

  @JsonProperty("components")
  protected List<DatePeriodHoursAndPeople> learningTimePerDate;
  @Override
  public List<Date> getDateComponents() {
    if (learningTimePerDate == null) {
      List<Date> datePeriods = new ArrayList<Date>();
      return datePeriods;
    }
    Collections.sort(learningTimePerDate, (dp1, dp2) -> dp1.getDate().compareTo(dp2.getDate()));
    List<Date> datePeriods = this.learningTimePerDate.stream().map(x -> (Date) x).collect(Collectors.toList());

    return datePeriods;
  }

  protected void setTargetThresholdValue() {
    for (Threshold threshold : thresholds) {
      for (Target target : threshold.getTargets()) {
        if(target instanceof CountHoursAndPeopleTarget) {
          List<DatePeriod> dateComponentsInCurrentPeriod = thresholdDatePeriodMap.get(threshold.getLevel()).getDateComponentsInCurrentPeriod();
          int count = 0;
          for(DatePeriod dateComponentInCurrentPeriod : dateComponentsInCurrentPeriod) {
            for(DatePeriodHoursAndPeople datePeriodAndPeople : learningTimePerDate) {
              if(datePeriodAndPeople.getDate().equals(dateComponentInCurrentPeriod.getDate())) {
                count += datePeriodAndPeople.getPeople() * datePeriodAndPeople.getHours();
              }
            }
          }
          ((CountHoursAndPeopleTarget) target).setPeopleAndHours(count);
        }else {
          super.setTargetThresholdValue();
        }
      }
    }
  }

  @Override
  public Map<String, Boolean> isActivityImplemented() {
    Map<String, Boolean> isImplementedMap = new HashMap<>();
    for (Threshold threshold : thresholds) {
      Boolean isImplemented = true;
      for (Target target : threshold.getTargets()) {
        if (target == null || target.implemented() == null || !target.implemented()) {
          logger.debug("is target.implemented() == null" + (target.implemented() == null));
          isImplemented = false;
        }
      }
      isImplementedMap.put(threshold.getLevel(), isImplemented);
    }

    return isImplementedMap;
  }
  @Override
  public HashMap<String, ThresholdDatePeriodManager> getThresholdDatePeriodMap() {
    return null;
  }
  public List<DatePeriodHoursAndPeople> getLearningTimePerDate() {
    return learningTimePerDate;
  }

  public void setLearningTimePerDate(List<DatePeriodHoursAndPeople> learningTimePerDate) {
    this.learningTimePerDate = learningTimePerDate;
  }
}

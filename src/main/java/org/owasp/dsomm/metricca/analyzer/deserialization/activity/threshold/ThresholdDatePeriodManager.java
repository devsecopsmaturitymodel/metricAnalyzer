package org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold;

import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Threshold;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DateComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ThresholdDatePeriodManager {
  private static final Logger logger = LoggerFactory.getLogger(ThresholdDatePeriodManager.class);

  private List<DatePeriod> thresholdDatePeriods;
  public ThresholdDatePeriodManager(Threshold threshold, List<Date> dates) {
    thresholdDatePeriods = new ArrayList<DatePeriod>();
      if (threshold.getDatePeriod() != null) for (Date component : dates) {
        thresholdDatePeriods.add(new DatePeriod(component.getDate(), threshold.getDatePeriod().getPeriodAsPeriod()));
      }
    thresholdDatePeriods.sort(Comparator.comparing(DatePeriod::getDate));
    Collections.sort(thresholdDatePeriods, (dp1, dp2) -> dp1.getDate().compareTo(dp2.getDate()));
  }

  public void setEndDatesBetweenPeriodAsInvisible() {
    int i = 0;
    for (DatePeriod component : this.thresholdDatePeriods) {
      if(component.getEndDate() == null) {
        logger.warn("EndDate is null, this should not happen");
        continue;
      }
      if(isDateBetweenTwoDates(component.getEndDate(), i)) {
        logger.debug("setEndDatesBetweenPeriodAsInvisible: " + component.getEndDate() + " is between two dates");
        component.setShowEndDate(false);
      } else {
        component.setShowEndDate(true);
      }
      i++;
    }
  }
  private boolean isDateBetweenTwoDates(java.util.Date date, int i) {
    for (;i< thresholdDatePeriods.size();i++ ) {
      DatePeriod datePeriod = thresholdDatePeriods.get(i);

      if(datePeriod.isInPeriod(date)) {
//        logger.info("isDateBetweenTwoDates: given " + date + " getDate " + datePeriod.getDate() + "enddate " + datePeriod.getEndDate());
        return true;
      }
    }
    return false;
  }
  public Integer getThresholdValue() {
    if(thresholdDatePeriods == null) {
      return 0;
    }
    return getDateComponentsInCurrentPeriod().size();
  }
  public List<DatePeriod> getDateComponentsInCurrentPeriod() {
    List<DatePeriod> periods = new ArrayList<DatePeriod>();
//    logger.info("getDateComponentsInCurrentPeriod: " + periods);
    for(DatePeriod component : thresholdDatePeriods) {
      if(component.isInPeriod(new java.util.Date())) {
        periods.add(component);
      }
    }
    for(DatePeriod component : periods) {
//      logger.info("getDateComponentsInCurrentPeriod after removal: " + component.getDate());
    }

    return periods;
  }


  public List<DatePeriod> getThresholdDatePeriods() {
    return thresholdDatePeriods;
  }

  public DatePeriod getDatePeriodForDate(java.util.Date date) {
    for(DatePeriod datePeriod : thresholdDatePeriods) {
      if(datePeriod.getDate().equals(date)) {
        return datePeriod;
      }
    }
    return null;
  }
  public DatePeriod getDatePeriodEndForDate(java.util.Date date) {
    for(DatePeriod datePeriod : thresholdDatePeriods) {
      if(datePeriod.getEndDate() == null) return null;
      if(datePeriod.getEndDate().equals(date)) {
        return datePeriod;
      }
    }
    return null;
  }
  public DatePeriod getDatePeriodEndForDateEnforced(java.util.Date date) {
    for(DatePeriod datePeriod : thresholdDatePeriods) {
      if(datePeriod.getEndDate(true).equals(date)) {
        return datePeriod;
      }
    }
    return null;
  }


  public void setThresholdDatePeriods(List<DatePeriod> thresholdDatePeriods) {
    this.thresholdDatePeriods = thresholdDatePeriods;
  }

  public DatePeriod getClosestBeforeDatePeriodComponent(java.util.Date date) {
    DatePeriod dateComponentToReturn = null;
    for (DatePeriod datePeriod : thresholdDatePeriods) {
      if (datePeriod.getDate().before(date)) {
        dateComponentToReturn = datePeriod;
      }
    }
    return dateComponentToReturn;
  }
  public List<java.util.Date> getStartAndEndDate() {
    List<java.util.Date> dates = new ArrayList<java.util.Date>();
    for(DatePeriod datePeriod : thresholdDatePeriods) {
      dates.add(datePeriod.getDate());
      dates.add(datePeriod.getEndDate(true));
    }
    return dates;
  }
}

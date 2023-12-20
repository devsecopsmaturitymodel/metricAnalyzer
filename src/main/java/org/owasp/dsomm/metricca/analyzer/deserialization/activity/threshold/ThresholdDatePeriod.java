package org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Target;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Threshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ThresholdDatePeriod {
  private static final Logger logger = LoggerFactory.getLogger(ThresholdDatePeriod.class);

  private List<DatePeriod> thresholdDatePeriods;
  public ThresholdDatePeriod(Threshold threshold, List<Date> dates) {
    thresholdDatePeriods = new ArrayList<DatePeriod>();
      if (threshold.getDatePeriod() != null) for (Date component : dates) {
        thresholdDatePeriods.add(new DatePeriod(component.getDate(), threshold.getDatePeriod().getPeriodAsPeriod()));
      }
    thresholdDatePeriods.sort(Comparator.comparing(DatePeriod::getDate));
  }

  public void setEndDatesBetweenPeriodAsInvisible() {
    int i = 0;
    for (DatePeriod component : this.thresholdDatePeriods) {
      if(component.getEndDate() == null) {
        logger.warn("EndDate is null, this should not happen");
        continue;
      }
      if(isDateBetweenTwoDates(component.getEndDate(), i)) {
        component.setShowEndDate(false);
      }
      i++;
    }
  }
  private boolean isDateBetweenTwoDates(java.util.Date date, int i) {
    for (;i< thresholdDatePeriods.size();i++ ) {
      DatePeriod datePeriod = thresholdDatePeriods.get(i);
      if(datePeriod.isInPeriod(date)) {
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
    logger.info("getDateComponentsInCurrentPeriod: " + periods);
    for(DatePeriod component : thresholdDatePeriods) {
      if(component.isInPeriod(new java.util.Date())) {
        periods.add(component);
      }
    }
    for(DatePeriod component : periods) {
      logger.info("getDateComponentsInCurrentPeriod after removal: " + component.getDate());
    }

    return periods;
  }


  public List<DatePeriod> getThresholdDatePeriods() {
    return thresholdDatePeriods;
  }

  public void setThresholdDatePeriods(List<DatePeriod> thresholdDatePeriods) {
    this.thresholdDatePeriods = thresholdDatePeriods;
  }
}

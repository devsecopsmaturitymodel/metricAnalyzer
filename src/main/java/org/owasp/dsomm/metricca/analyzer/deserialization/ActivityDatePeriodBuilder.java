package org.owasp.dsomm.metricca.analyzer.deserialization;

import org.joda.time.Period;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.DatePeriodActivity;

public class ActivityDatePeriodBuilder extends ActivityBuilder {
  protected DatePeriodActivity activity;

  public ActivityDatePeriodBuilder(DatePeriodActivity activity) throws InstantiationException, IllegalAccessException {
    super(activity);
  }

  public ActivityDatePeriodBuilder withDatePeriod(Period datePeriod) {
    activity.setPeriod(datePeriod);
    return this;
  }

}
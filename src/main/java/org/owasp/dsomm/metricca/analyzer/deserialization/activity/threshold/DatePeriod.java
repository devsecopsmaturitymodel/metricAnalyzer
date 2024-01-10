package org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePeriod {
  private static final Logger logger = LoggerFactory.getLogger(DatePeriod.class);
  protected java.util.Date date;
  @JsonIgnore
  protected Period period;
  protected Boolean isShowEndDate = true;

  public DatePeriod(java.util.Date date, Period period) {
    this.date = date;
    this.period = period;
  }

  public Period getPeriod() {
    return period;
  }

  public void setPeriod(Period period) {
    this.period = period;
  }

  @JsonProperty("period")
  public String getPeriodAsString() {
    if (period == null) {
      return null;
    }
    return period.toString();
  }

  public Boolean isInPeriod(java.util.Date givenDate) {
    return givenDate.after(this.date) && givenDate.before(this.getEndDate(true));
  }

  public java.util.Date getEndDate(boolean enforceShowEndDate) {
    if (this.period == null) {
      return null;
    }
    if (!enforceShowEndDate && !getShowEndDate()) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(this.date);
    c.add(Calendar.HOUR, this.period.getHours());
    c.add(Calendar.DATE, this.period.getDays()); // TODO Check in unit test
    c.add(Calendar.MONTH, this.period.getMonths());
    c.add(Calendar.YEAR, this.period.getYears());
    java.util.Date enddate = c.getTime();
    return enddate;
  }

  public java.util.Date getEndDate() {
    return getEndDate(false);
  }


  public java.util.Date getDate() {
    return date;
  }

  public void setDate(String date) throws ParseException {
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    this.date = sdf1.parse(date);
  }

  public Boolean getShowEndDate() {
    return isShowEndDate;
  }

  public void setShowEndDate(Boolean showEndDate) {
    logger.debug("showEndDate: " + showEndDate);
    isShowEndDate = showEndDate;
  }
}

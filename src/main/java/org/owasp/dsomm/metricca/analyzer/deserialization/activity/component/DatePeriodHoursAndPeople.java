package org.owasp.dsomm.metricca.analyzer.deserialization.activity.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod;

public class DatePeriodHoursAndPeople extends Date {
  private Integer hours = Integer.MIN_VALUE;

  private Integer people = Integer.MIN_VALUE;

  public Integer getHours() {
    return hours;
  }

  public Integer getPeople() {
    return people;
  }

  public void setHours(Integer hours) {
    this.hours = hours;
  }

  public void setPeople(Integer people) {
    this.people = people;
  }
  public Integer getHoursAndPeople() {
    if(people!= null && hours != null) {
      return people * hours;
    } else {
      return null;
    }
  }
}

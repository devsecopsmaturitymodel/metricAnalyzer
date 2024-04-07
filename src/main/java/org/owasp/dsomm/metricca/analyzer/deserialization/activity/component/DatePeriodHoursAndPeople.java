package org.owasp.dsomm.metricca.analyzer.deserialization.activity.component;

public class DatePeriodHoursAndPeople extends Date {
  private Double hours = Double.MIN_VALUE;

  private Integer people = Integer.MIN_VALUE;

  public Double getHours() {
    return hours;
  }

  public void setHours(Double hours) {
    this.hours = hours;
  }

  public Integer getPeople() {
    return people;
  }

  public void setPeople(Integer people) {
    this.people = people;
  }

  public Double getHoursAndPeople() {
    if (people != null && hours != null) {
      return people * hours;
    } else {
      return null;
    }
  }
}

package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;

import java.util.Date;

public class DateActivity extends Activity {
  @JsonProperty("date")
  Date date;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}

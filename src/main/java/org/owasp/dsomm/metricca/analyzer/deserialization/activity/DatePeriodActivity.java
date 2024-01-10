package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class DatePeriodActivity extends Activity {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  @JsonProperty("components")
  List<Date> dates;

  @Override
  public List<Date> getDateComponents() {
    if (this.dates == null) {
      return Collections.emptyList();
    }
    return dates;
  }

  public List<Date> getDates() {
    return dates;
  }

  public void setDates(List<Date> dates) {
    this.dates = dates;
  }
}

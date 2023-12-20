package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.DateLinkTitle;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Threshold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DateTitleAndLinkActivity extends DatePeriodActivity {
  @JsonProperty("dateLinkTitles")
  protected List<DateLinkTitle> dateLinkTitles;

  public List<DateLinkTitle> getDateLinkTitles() {
    return dateLinkTitles;
  }

  public void setDateLinkTitles(List<DateLinkTitle> dateLinkTitles) {
    this.dateLinkTitles = dateLinkTitles;
  }

  @Override
  public List<Date> getDateComponents() {
    return this.dateLinkTitles.stream().map(x -> (Date)x).collect(Collectors.toList());
  }
}

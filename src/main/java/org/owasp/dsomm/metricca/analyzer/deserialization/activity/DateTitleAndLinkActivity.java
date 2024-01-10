package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.DateLinkTitle;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DateTitleAndLinkActivity extends Activity {
  @JsonProperty("components")
  protected List<DateLinkTitle> dateLinkTitles;

  public List<DateLinkTitle> getDateLinkTitles() {
    return dateLinkTitles;
  }

  public void setDateLinkTitles(List<DateLinkTitle> dateLinkTitles) {
    this.dateLinkTitles = dateLinkTitles;
  }

  @Override
  public List<Date> getDateComponents() {
    if (this.dateLinkTitles == null) {
      return Collections.emptyList();
    }
    return this.dateLinkTitles.stream().map(x -> (Date) x).collect(Collectors.toList()); // sort
  }
}

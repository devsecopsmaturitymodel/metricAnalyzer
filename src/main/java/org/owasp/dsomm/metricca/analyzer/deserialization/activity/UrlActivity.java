package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Url;

import java.util.*;
import java.util.stream.Collectors;

public class UrlActivity extends DatePeriodActivity {
  @JsonProperty("urls")
  protected List<Url> urls;

  public List<Url> getUrls() {
    return urls;
  }

  public void setUrls(List<Url> urls) {
    this.urls = urls;
  }



  @Override
  public List<Date> getDateComponents() {
    if(urls == null) {
      List<Date> datePeriods = new ArrayList<Date>();
      return datePeriods;
    }
    Collections.sort(urls, (dp1, dp2) -> dp1.getDate().compareTo(dp2.getDate()));
    List<Date> datePeriods = this.urls.stream().map(x -> (Date)x).collect(Collectors.toList());

    return datePeriods;
  }
}

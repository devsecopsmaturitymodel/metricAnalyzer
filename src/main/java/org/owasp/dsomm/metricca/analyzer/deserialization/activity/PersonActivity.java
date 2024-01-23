package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PersonActivity extends Activity {
  @JsonProperty("components")
  protected List<Person> champions;

  public PersonActivity() {
    super();
  }

  @Override
  public List<Date> getDateComponents() {
    if (champions == null) {
      List<Date> datePeriods = new ArrayList<Date>();
      return datePeriods;
    }
    Collections.sort(champions, (dp1, dp2) -> dp1.getDate().compareTo(dp2.getDate()));
    List<Date> datePeriods = this.champions.stream().map(x -> (Date) x).collect(Collectors.toList());

    return datePeriods;
  }

  public List<Person> getChampions() {
    return champions;
  }

  public void setChampions(List<Person> champions) {
    this.champions = champions;
  }
}

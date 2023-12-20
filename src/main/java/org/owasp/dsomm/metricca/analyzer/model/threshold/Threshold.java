package org.owasp.dsomm.metricca.analyzer.model.threshold;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Component;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DateComponent;

import java.util.ArrayList;
import java.util.List;


public class Threshold {

  @JsonProperty("level")
  String level;

  @JsonProperty("type")
  String type;
  @JsonProperty("targets")
  private List<Target> targets;
  @JsonProperty("dateperiod")
  private DatePeriod datePeriod;

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Boolean getThresholdReached(ArrayList<Component> componentArrayList) {
    ArrayList<Component> componentsToCheck = new ArrayList<Component>();
    if (getDatePeriod() == null) {
      componentsToCheck =  componentArrayList;
    } else {
      for (Component component : componentArrayList) {
        if (component instanceof DateComponent dateComponent) {
            //                    if(dateComponent.getValue().after(getDatePeriod().getPeriod())) {
          componentsToCheck.add(component);
//                    }
        }
      }
    }


    Boolean isThresholdReached = true;
    for (Target target : targets) {
      if (target.thresholdReached() != null && !target.thresholdReached()) {
        isThresholdReached = false;
      }
    }
    return isThresholdReached;
  }

  public List<Target> getTargets() {
    return targets;
  }

  public void setTargets(List<Target> targets) {
    this.targets = targets;
  }

  public DatePeriod getDatePeriod() {
    return datePeriod;
  }

  public void setDatePeriod(DatePeriod datePeriod) {
    this.datePeriod = datePeriod;
  }


}

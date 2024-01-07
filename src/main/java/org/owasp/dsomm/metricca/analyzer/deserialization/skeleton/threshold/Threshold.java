package org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.target.CountTarget;

import java.util.List;


public class Threshold {

  @JsonProperty("level")
  String level;

  @JsonProperty("type")
  String type;
  @JsonProperty("targets")
  private List<Target> targets;
  @JsonProperty("period")
  private Period period;

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

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  public String getDescription() {
    String description = "";
    for (Target target : targets) {
      if (target instanceof CountTarget) {
        description += "Min value: " + ((CountTarget) target).getMinValue() + " max value: " + ((CountTarget) target).getMaxValue();
      }
      description += " count: " + ((CountTarget) target).getCount() + "\n";
    }
    return description;
  }

//  public Boolean getThresholdReached(ArrayList<Component> componentArrayList) {
//    ArrayList<Component> componentsToCheck = new ArrayList<Component>();
//    if (getDatePeriod() == null) {
//      componentsToCheck =  componentArrayList;
//    } else {
//      for (Component component : componentArrayList) {
//        if (component instanceof DateComponent dateComponent) {
//            //                    if(dateComponent.getValue().after(getDatePeriod().getPeriod())) {
//          componentsToCheck.add(component);
////                    }
//        }
//      }
//    }
//
//
//    Boolean isThresholdReached = true;
//    for (Target target : targets) {
//      if (target.implemented() != null && !target.implemented()) {
//        isThresholdReached = false;
//      }
//    }
//    return isThresholdReached;
//  }

  public List<Target> getTargets() {
    return targets;
  }

  public void setTargets(List<Target> targets) {
    this.targets = targets;
  }

  public Period getDatePeriod() {
    return period;
  }

  public void setDatePeriod(Period period) {
    this.period = period;
  }


}

package org.owasp.dsomm.metricca.analyzer.model.threshold;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Component;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DateComponent;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DatePeriodEndComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Threshold {

    @JsonProperty("level")
    String level;

    @JsonProperty("type")
    String type;
    enum Types {
        activity,
        component,
    }

    @JsonProperty("targets")
    private ArrayList<Target> targets;

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
        if(getDatePeriod() == null) {
            componentsToCheck = componentArrayList;
        } else {
            for(Component component : componentArrayList) {
                if(component instanceof DateComponent) {
                    DateComponent dateComponent = (DateComponent) component;
//                    if(dateComponent.getValue().after(getDatePeriod().getPeriod())) {
                        componentsToCheck.add(component);
//                    }
                }
            }
        }



        Boolean isThresholdReached = true;
        for(Target target : targets) {
            if(!target.thresholdReached()) {
                isThresholdReached = false;
            }
        }
        return isThresholdReached;
    }

    public ArrayList<Target> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<Target> targets) {
        this.targets = targets;
    }

    public DatePeriod getDatePeriod() {
        return datePeriod;
    }

    public void setDatePeriod(DatePeriod datePeriod) {
        this.datePeriod = datePeriod;
    }
}

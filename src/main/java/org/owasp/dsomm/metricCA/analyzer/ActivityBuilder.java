package org.owasp.dsomm.metricCA.analyzer;

import org.owasp.dsomm.metricCA.analyzer.components.DateComponent;
import org.owasp.dsomm.metricCA.analyzer.components.IntComponent;
import org.owasp.dsomm.metricCA.analyzer.components.StringComponent;

import java.util.ArrayList;

public class ActivityBuilder {

    private Activity activity;

    public ActivityBuilder() {
        this.activity = new Activity();
    }

    public ActivityBuilder setActivityName(String name) {
        activity.setActivityName(name);
        return this;
    }

    public ActivityBuilder setLevel(String level) {
        activity.setLevel(level);
        return this;
    }

    public ActivityBuilder addStringComponent(String componentName, ArrayList<String> nester) {
        StringComponent comp = new StringComponent();
        comp.setName(componentName);
        activity.addComponent(comp, nester);
        return this;
    }
    
    public ActivityBuilder addIntComponent(String componentName, ArrayList<String> nester) {
        IntComponent comp = new IntComponent();
        comp.setName(componentName);
        activity.addComponent(comp, nester);
        return this;
    }

    public ActivityBuilder addDateComponent(String componentName, ArrayList<String> nester) {
        DateComponent comp = new DateComponent();
        comp.setName(componentName);
        activity.addComponent(comp, nester);
        return this;
    }

    public Activity build() {
        return this.activity;
    }
}

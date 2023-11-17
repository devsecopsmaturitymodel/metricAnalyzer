package com.analyzer;

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

    public ActivityBuilder addStringComponent(String componentName) {
        StringComponent comp = new StringComponent();
        comp.setName(componentName);
        activity.addComponent(comp);
        return this;
    }
    
    public ActivityBuilder addIntComponent(String componentName) {
        IntComponent comp = new IntComponent();
        comp.setName(componentName);
        activity.addComponent(comp);
        return this;
    }

    public Activity build() {
        return this.activity;
    }
}

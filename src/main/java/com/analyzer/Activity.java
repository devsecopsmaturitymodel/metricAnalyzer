package com.analyzer;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private String level;
    private String activityName;
    private List<Component> components;

    public Activity() {
        components = new ArrayList<>();
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public List<Component> getComponent() {
        return this.components;
    }

}

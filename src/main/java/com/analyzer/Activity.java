package com.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity {
    private String level;
    private String activityName;
    private Map<String, Object> components;

    public Activity() {
        components = new HashMap<>();
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

    public void addComponent(Component component, ArrayList<String> nester) {
        if (nester.isEmpty()) {
            components.put(component.getName(), component);
        } else {
            
            for (String key : nester) {
                if (components.get(nester.get(0)) != null){ // Is key in components
                    Map<String, Object> temp = (HashMap) components.get(key);
                    temp.put(component.getName(), component);
                }
                else {
                    Map<String, Object> temp = new HashMap<>();
                    temp.put(component.getName(), component);
                    components.put(key, temp);
                }
            }
        }
    }

    public Map<String, Object> getComponents() {
        return this.components;
    }
}

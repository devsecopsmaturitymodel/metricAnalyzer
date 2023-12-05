package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Activity {
    private static final Logger logger = LoggerFactory.getLogger(Activity.class);

    private String level;
    private String activityName;


    @JsonIgnore
    private Map<String, Object> components;
    private ArrayList<Map<String, Object>> content;

    public Activity() {
        components = new HashMap<>();
        content = new ArrayList<>();
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
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

    public void addContentSkeleton() {
        HashMap finalAcc = new HashMap<>();
        for (String componentKey : components.keySet()){
            if (components.get(componentKey) instanceof Component){
                try {
                    Component component = (Component) components.get(componentKey);
                    finalAcc.put(componentKey, component.clone());
                } catch (CloneNotSupportedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if (components.get(componentKey) instanceof HashMap) {
                HashMap temp = new HashMap<>();
                HashMap fin = (HashMap) components.get(componentKey);
                for (Object k : fin.keySet()){
                    Component component = (Component) fin.get(k);
                    try {
                        temp.put(k, component.clone());
                    } catch (CloneNotSupportedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                }
                finalAcc.put(componentKey, temp);
            }
            else {
                // TODO Raise an exception
                System.out.println("This instance should not be here!");
            }
        }
        content.add(finalAcc);
    }

    public ArrayList<Map<String, Object>> getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + activityName + ", level=" + level + "]";
    }
}

package org.owasp.dsomm.metricCA.analyzer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
public class Activity implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(Activity.class.getName());

    @Id
    private String level;

    @Id
    private String activityName;

    @Id
    private String team = "noteam";

    private Map<String, Object> components;

    @OneToMany
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

    public void addContent() {
        HashMap finalAcc = new HashMap<>();
        for (String componentKey : components.keySet()){
            if (components.get(componentKey) instanceof Component){
                try {
                    LOGGER.log(Level.INFO, "Key " + componentKey);
                    Component component = (Component) components.get(componentKey);
                    LOGGER.log(Level.INFO, "Value " + component.getValue());
                    finalAcc.put(componentKey, component.clone());
                } catch (CloneNotSupportedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if (components.get(componentKey) instanceof HashMap){
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

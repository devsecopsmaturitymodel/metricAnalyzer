package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.owasp.dsomm.metricCA.analyzer.exception.ComponentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActivityDirector {
    private static final Logger logger = LoggerFactory.getLogger(ActivityDirector.class);

    private Map<String, Activity> activities;
    private ArrayList<String> nester;

    public ActivityDirector() {
        this.activities = new HashMap<>();
        this.nester = new ArrayList<String>();
    }

    public void createActivities(Map<?, ?> javaYaml) {
        for (Map.Entry<?, ?> entry : javaYaml.entrySet()) {
            String key = (String) entry.getKey();
            LinkedHashMap<?, ?> value = (LinkedHashMap<?, ?>) entry.getValue();
            //System.out.println(value);

            createActivity(key.toString(), value);
        }
    }

    private void createActivity(String activityName, LinkedHashMap<?, ?> data){
        // Initializes a new Activity Builder, creating a corresponding Activity along with an empty ArrayList for its components.
        ActivityBuilder builder = new ActivityBuilder();

        // Get Level
        String level = (String) data.get("level");

        builder = builder
            .setActivityName(activityName)
            .setLevel(level);

        // Add Components in the builder
        ArrayList arr = (ArrayList) data.get("components");
        addComponents(builder, arr);
        
        Activity activity = builder.build();

        // Add Activity to the HashMap
        activities.put(activityName, activity);
    }

    private void addComponents(ActivityBuilder builder, ArrayList data) {
        LinkedHashMap components = (LinkedHashMap) data.get(0);
        List<Object> keyList = new ArrayList<>(components.keySet());

        for (int i = 0; i < keyList.size(); i++) {
            Object key = keyList.get(i);
            Object value = components.get(key);

            if (value instanceof String) {
                switch (value.toString()) {
                    case "string":
                        builder.addStringComponent(key.toString(), nester);
                        break;
                    case "date":
                        builder.addDateComponent(key.toString(), nester);
                        break;
                    case "dateperiod":
                        builder.addDatePeriodComponent(key.toString(), "365", true, nester);
//                        builder.addDatePeriodEndComponent("end", "365", true, nester);
                        break;
                    case "int":
                        builder.addIntComponent(key.toString(), nester);
                        break;
                    default:
                        throw new ComponentNotFoundException("Component '" + value + "' doesn't exists");
                }
            }
            else if (value instanceof ArrayList && nester.isEmpty()) {
                nester.add(key.toString());
                ArrayList<Object> arr = (ArrayList<Object>) value;
                addComponents(builder, arr);
                nester = new ArrayList<String>();
            }
            else if (value instanceof ArrayList && !nester.isEmpty()) {
                nester.add(key.toString());
                ArrayList<Object> arr = (ArrayList<Object>) value;
                addComponents(builder, arr);
            }
            else {
                // TODO: Should throw an exception! 
                System.out.println("This instance does not exist!");
            }
            //System.out.println("Key: " + key + ", Value: " + value);
        }
    }

    public Map<String, Activity> getActivities() {
        return activities;
    }
}

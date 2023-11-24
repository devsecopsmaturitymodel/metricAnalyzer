package com.analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Application {
    private ActivityDirector activityDirector;

    public Application(Map<?, ?> configJavaYaml) {
        activityDirector = new ActivityDirector();
        activityDirector.createActivities(configJavaYaml);
    }

    public void saveData(Map<?, ?> appJavaYaml){
        HashMap allActivities = (HashMap) (appJavaYaml.get("activities"));
        for (Object actKey : allActivities.keySet()){
            ArrayList data = (ArrayList) allActivities.get(actKey);
            for (int i = 0; i<data.size(); i++){
                activityDirector.getActivities().get(actKey).addContent();
            }
            fillActivityContent(data, actKey);
        }
    }

    private void fillActivityContent(ArrayList data, Object activity){
        for (int i = 0; i<data.size(); i++){ // Data in each activity
            HashMap<String, Object> temp_data = (HashMap<String, Object>) data.get(i);
            Set<Entry<String, Object>> entrySet = temp_data.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                Object mayComp = entry.getValue();
                if (mayComp.getClass() == ArrayList.class) {
                    ArrayList<?> arrayList = (ArrayList<?>) mayComp;
                    HashMap f = (HashMap) arrayList.get(0);
                    Set<Entry<String, Object>> enSet2= f.entrySet();
                    for (Entry<String, Object> entry2 : enSet2) {
                        HashMap innerMap = (HashMap) activityDirector.getActivities().get(activity).getContent().get(i).get(entry.getKey());
                        Component compon = (Component) (innerMap.get(entry2.getKey()));
                        compon.setValue(entry2.getValue());
                    }
                }
                else {
                    HashMap comp = temp_data;
                    Component editComp = (Component) activityDirector.getActivities().get(activity).getContent().get(i).get(comp.keySet().iterator().next());
                    editComp.setValue(comp.values().iterator().next());
                }
            }
        }
    }

    public Collection<Activity> getActivities() {
        Collection<Activity> activities = activityDirector.getActivities().values();
        return activities;
    }
}

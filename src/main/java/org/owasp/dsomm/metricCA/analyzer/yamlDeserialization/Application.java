package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DatePeriodComponent;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DatePeriodEndComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private ActivityDirector activityDirector;

    public Application(Map<?, ?> configJavaYaml) {
        activityDirector = new ActivityDirector();
        activityDirector.createActivities(configJavaYaml);
    }

    public void saveData(Map<?, ?> appJavaYaml){
        HashMap allActivities = (HashMap) (appJavaYaml.get("activities"));
        for (Object activityKey : allActivities.keySet()){
            ArrayList data = (ArrayList) allActivities.get(activityKey);
            for (int i = 0; i<data.size(); i++){
                activityDirector.getActivities().get(activityKey).addContent();
            }
            fillActivityContent(data, activityKey);
        }
    }

    private void fillActivityContent(ArrayList data, Object activity) {
        logger.info("data"  + data);
        for (int i = 0; i<data.size(); i++){ // Data in each activity
            HashMap<String, Object> temp_data = (HashMap<String, Object>) data.get(i);
            Set<Entry<String, Object>> entrySet = temp_data.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                Object mayComp = entry.getValue();
                if (mayComp.getClass() == ArrayList.class) {
                    logger.info("test");
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
                    Object value = (Object) comp.values().iterator().next();
                    editComp.setValue(value);
//                    if(editComp instanceof DatePeriodComponent) {
//                        DatePeriodEndComponent editCompEnd = (DatePeriodEndComponent) activityDirector.getActivities().get(activity).getContent().get(i).get("end");
//                        editCompEnd.setValue(value);
//                        editCompEnd.setActive(false);
//                        editCompEnd.setName(editComp.getName());
//                        logger.info("value2" +  editCompEnd.getValue());
//                    }
                }
            }
        }
    }

    public Collection<Activity> getActivities() {
        Collection<Activity> activities = activityDirector.getActivities().values();
        return activities;
    }
}

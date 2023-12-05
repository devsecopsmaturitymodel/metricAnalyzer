package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DatePeriodComponent;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DatePeriodEndComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private String team;
    private String applicationId;
    private ActivityDirector activityDirector;

    public Application(Map<?, ?> configJavaYaml) {
        activityDirector = new ActivityDirector();
        activityDirector.createActivities(configJavaYaml);
    }

    public void saveData(Map<?, ?> appJavaYaml) {
        HashMap allActivities = (HashMap) (appJavaYaml.get("activities"));
        for (Object activityKey : allActivities.keySet()) {
            ArrayList data = (ArrayList) allActivities.get(activityKey);
            for (int i = 0; i < data.size(); i++) {
                activityDirector.getActivities().get(activityKey).addContentSkeleton();
            }
            fillActivityContent(data, activityKey);
        }
        enrichComponentsOfActivities();
    }

    private void fillActivityContent(ArrayList data, Object activity) {
        for (int i = 0; i < data.size(); i++) { // Data in each activity
            HashMap<String, Object> temp_data = (HashMap<String, Object>) data.get(i);
            Set<Entry<String, Object>> entrySet = temp_data.entrySet();
            HashMap comp = temp_data;
            Iterator<Object> iterator = comp.keySet().iterator();
            Iterator<Object> valueIterator = comp.values().iterator();
            for (Entry<String, Object> entry : entrySet) {
                Object mayComp = entry.getValue();
                if (mayComp.getClass() == ArrayList.class) {
                    ArrayList<?> arrayList = (ArrayList<?>) mayComp;
                    HashMap f = (HashMap) arrayList.get(0);
                    Set<Entry<String, Object>> enSet2 = f.entrySet();
                    for (Entry<String, Object> entry2 : enSet2) {
                        HashMap innerMap = (HashMap) activityDirector.getActivities().get(activity).getContent().get(i).get(entry.getKey());
                        Component compon = (Component) (innerMap.get(entry2.getKey()));
                        compon.setValue(entry2.getValue());
                    }
                } else {
                    Component editComp = (Component) activityDirector.getActivities().get(activity).getContent().get(i).get(iterator.next());
                    Object value = (Object) valueIterator.next();
                    editComp.setValue(value);
                }
            }
        }
    }

    public Collection<Activity> getActivities() {
        Collection<Activity> activities = activityDirector.getActivities().values();
        return activities;
    }

    private String mapKeyForCompare;

    private String getMapKeyForCompare(ArrayList<Map<String, Object>> content) {
        for (Map<String, Object> componentMap : content) {
            for (String key : componentMap.keySet()) {
                if (key.contains("date"))
                    return key;
            }
        }
        return "";
    }

    private void sortContent(Activity activity) {
        this.mapKeyForCompare = getMapKeyForCompare(activity.getContent());
        Comparator<Map<String, Object>> dateComparator = (map1, map2) -> {
            Date date1 = (Date) ((org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Component) map1.get(this.mapKeyForCompare)).getValue();
            Date date2 = (Date) ((Component) map2.get(this.mapKeyForCompare)).getValue();
            return date1.compareTo(date2);
        };
        Collections.sort(activity.getContent(), dateComparator);
    }

    private Collection<Activity> enrichComponentsOfActivities() {
        Iterator<Activity> iterator = getActivities().iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            this.sortContent(activity);

            boolean containsDate = false;
            ArrayList<Map<String, Object>> newContent = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < activity.getContent().size(); i++) {
                Map<String, Object> componentMap = activity.getContent().get(i);
                for (String key : componentMap.keySet()) {
                    if (key.contains("date")) {
                        containsDate = true;
                        break;
                    }
                }
                if (containsDate) {
                    for (Object component : componentMap.values()) {
                        if (component instanceof DatePeriodComponent && !(component instanceof DatePeriodEndComponent)) {
                            DatePeriodEndComponent end = new DatePeriodEndComponent(((DatePeriodComponent) component).getPeriod());
                            end.setName(((DatePeriodComponent) component).getName());
                            end.setValue(((DatePeriodComponent) component).getValue());
                            HashMap<String, Object> content = new HashMap<String, Object>();
                            content.put(((DatePeriodComponent) component).getName(), end);
                            if (isPeriodBetweenTwoDates(activity, i, end.getValue())) {
                                continue;
                            }
                            newContent.add(content);
                        }
                    }
                }
            }
            activity.getContent().addAll(newContent);
        }
        return getActivities();
    }

    private boolean isPeriodBetweenTwoDates(Activity activity, int currentContentIndex, Date endOfPeriod) {
        try {
            activity.getContent().get(currentContentIndex+1);
        }catch (java.lang.IndexOutOfBoundsException e) {
            return false;
        }

        Date dateAfterStart = null;
            Map<String, Object> componentMap = activity.getContent().get(currentContentIndex+1);
            for (Object component : componentMap.values()) {
                if (component instanceof DatePeriodComponent && !(component instanceof DatePeriodEndComponent)) {
                        dateAfterStart = ((DatePeriodComponent) component).getValue();
                        break;
                    }
                }

        logger.info(" dateAfterStart " + dateAfterStart + " endOfPeriod " + endOfPeriod);
        if (dateAfterStart == null) return false;
        if (dateAfterStart.before(endOfPeriod)) {
            return true;
        }
        return false;
    }
    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}

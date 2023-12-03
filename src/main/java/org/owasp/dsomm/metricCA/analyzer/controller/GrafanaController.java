package org.owasp.dsomm.metricCA.analyzer.controller;

import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Activity;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Component;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.YamlToObjectManager;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DatePeriodComponent;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DatePeriodEndComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class GrafanaController {
    private static final Logger logger = LoggerFactory.getLogger(GrafanaController.class);


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Activity> getActivities() {


        return getEnrichedActivities();

    }

    private Collection<Activity> getEnrichedActivities() {
        Collection<Activity> activities = YamlToObjectManager.getActivities();;
        Comparator<Map<String, Object>> dateComparator = (map1, map2) -> {
            Date date1 = (Date) ((Component) map1.get("read date")).getValue();
            Date date2 = (Date) ((Component) map2.get("read date")).getValue();
            return date1.compareTo(date2);
        };

        for (int j=0; j<activities.size();j++) {
            Activity activity = activities.iterator().next();
            Collections.sort(activity.getContent(), dateComparator);
            boolean containsDate = false;
            for(int i = 0; i<activity.getContent().size(); i++) {
                Map<String, Object> componentMap = activity.getContent().get(i);
                for (String key : componentMap.keySet()) {
                    if(key.contains("date")) {
                        containsDate = true;
                        break;
                    }
                }
                if (containsDate) {
                    for (Object component : componentMap.values()) {
                        logger.info("value" + component);
                        if (component instanceof DatePeriodComponent && !(component instanceof DatePeriodEndComponent)) {
                            DatePeriodEndComponent end = new DatePeriodEndComponent(((DatePeriodComponent) component).getPeriodInDays());
                            end.setName(((DatePeriodComponent) component).getName() + "q");
                            end.setValue(((DatePeriodComponent) component).getValue());
                            HashMap<String, Object> content = new HashMap<String, Object>();
                            content.put(((DatePeriodComponent) component).getName(), end);
                            activity.getContent().add(content);
                        }
                    }
                }
            }
        }

        return activities;
    }
}
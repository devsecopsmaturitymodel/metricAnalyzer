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

        for(int j=0; j<activities.size();j++) {
            Activity activity = activities.iterator().next();
            Collections.sort(activity.getContent(), dateComparator);
            boolean containsDate = false;
            for(int i = 0; i<activity.getContent().size(); i++) {
                Map<String, Object> componentMap = activity.getContent().get(i);
                logger.info("component " + componentMap);
                for (String key : componentMap.keySet()) {
                    if(key.contains("date")) {
                        containsDate = true;
                        break;
                    }
                }
                if (containsDate) { // TODO
                    for (Object value : componentMap.values()) {
                        if (componentMap instanceof DatePeriodComponent) {
                            logger.info("adding");
                            DatePeriodEndComponent end = new DatePeriodEndComponent();
                            end.setName(((DatePeriodComponent) componentMap).getName());
                            end.setValue(((DatePeriodComponent) componentMap).getValue());
                            end.setPeriodInDays(((DatePeriodComponent) componentMap).getPeriodInDays());
                            HashMap<String, Object> content = new HashMap<String, Object>();
                            content.put(((DatePeriodComponent) componentMap).getName(), end);
                            activity.getContent().add(content);
                        }
                    }

                }


            }
        }

        return activities;
    }
}
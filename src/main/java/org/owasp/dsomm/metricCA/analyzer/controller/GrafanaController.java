package org.owasp.dsomm.metricCA.analyzer.controller;

import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Activity;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.YamlToObjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class GrafanaController {
    private static final Logger logger = LoggerFactory.getLogger(GrafanaController.class);


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Activity> getActivities() {
        return YamlToObjectManager.getActivities();
    }
    @GetMapping("/hello-world")
    @ResponseBody
    public String sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
        return "x";
    }
}
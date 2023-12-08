package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component

public class YamlToObjectManagerInit {
    @Autowired
    private ApplicationDirector applicationDirector;

    @PostConstruct
    private void postConstruct() throws Exception {
        applicationDirector.getApplications();
    }
}

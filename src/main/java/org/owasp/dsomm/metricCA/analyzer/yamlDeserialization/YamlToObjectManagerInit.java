package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;

@Component

public class YamlToObjectManagerInit {
    @Autowired
    private YamlToObjectManager yamlToObjectManager;

    @PostConstruct
    private void postConstruct() throws FileNotFoundException {
        yamlToObjectManager.getApplications();
    }
}

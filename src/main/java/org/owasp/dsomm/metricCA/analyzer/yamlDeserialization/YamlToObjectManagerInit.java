package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component

public class YamlToObjectManagerInit {
    @Autowired
    private YamlToObjectManager yamlToObjectManager;

    @PostConstruct
    private void postConstruct() throws Exception {
        yamlToObjectManager.getApplications();
    }
}

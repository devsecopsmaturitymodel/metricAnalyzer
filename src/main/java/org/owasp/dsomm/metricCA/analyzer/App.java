package org.owasp.dsomm.metricCA.analyzer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;


import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger( App.class.getName() );

    private static Namespace getNamespace(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("App").build()
                .description("metric Analyzer. Provide YAMLs to be parsed and populated for metrics with a threshold.");
        parser.addArgument("--configuration-yaml-path")
                .dest("configurationPath")
                .setDefault("./definitions/configuration.yaml")
                .help("Path to configuration.yaml on the filesystem");
        parser.addArgument("--application-yaml-path")
                .dest("applicationPath")
                .setDefault("./activity_definitions/App1.yaml")
                .help("Path to application.yaml (or similar) on the filesystem. This parameter might be changed to a folder path");
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
            return ns;
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        return null;
    }
    public static void main(String[] args) {
        // TODO: Scanner which gets all yaml files the configuration.yaml file --> put it in utils
        Namespace ns = getNamespace(args);
        String yamlConfigurationFilePath = ns.getString("configurationPath");
        assert yamlConfigurationFilePath != null;
        LOGGER.log(Level.INFO, "yamlConfigurationFilePath " + yamlConfigurationFilePath );

        // TODO: utils
        Map<?, ?> configJavaYaml = YamlReader.convertYamlToJavaYaml(yamlConfigurationFilePath);

        // Read App yaml TODO: Scanner
        String appPath = ns.getString("applicationPath");
        Map<?, ?> app1JavaYaml = YamlReader.convertYamlToJavaYaml(appPath);

        // Create App Class
        Application newApp = new Application(configJavaYaml);
        assert app1JavaYaml != null;
        newApp.saveData(app1JavaYaml);

        // Example

        ObjectMapper mapper = new ObjectMapper();

        try {
            // convert user o,bject to json string and return it
            LOGGER.log(Level.INFO,"Full object\n" + mapper.writeValueAsString(newApp.getActivities()));
        }
        catch (JsonProcessingException e) {
            // catch various errors
            e.printStackTrace();
        }

        Collection<Activity> activities = newApp.getActivities();
        for (Activity activity : activities) {
            Activity ac = activity;
            LOGGER.log(Level.INFO, "Activity: " + activity);
            System.out.println(((Activity) activity).getComponents());
            Component cmp = (Component) (ac.getContent().get(0).get("read date"));
            System.out.println(cmp.getValue());
        }


    }
}

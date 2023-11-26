package org.owasp.dsomm.metricCA.analyzer;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


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
        Collection activities = newApp.getActivities();




        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Session session = sessionFactory.openSession();
        for (Object activity : activities) {
            session.save(activity);
        }

        session.close();
        sessionFactory.close();

        // Example
//
//        ObjectMapper mapper = new ObjectMapper();
//
//
//        String activitiesAsJsonString = null;
//        try {
//            activitiesAsJsonString = mapper.writeValueAsString(activities);
//
//            Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(activitiesAsJsonString);
//            System.out.println("###############################");
//            System.out.println(flattenJson);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        // convert user o,bject to json string and return it
//        LOGGER.log(Level.INFO,"Full object\n" + activitiesAsJsonString);
//
//
//        for (Object activity : activities) {
//            Activity ac = (Activity) activity;
//
//            try {
//
//                String activityAsJsonString = mapper.writeValueAsString(activity);
//                // convert user o,bject to json string and return it
//                LOGGER.log(Level.INFO,"Full activity object\n" + activityAsJsonString);
//
//            }
//            catch (JsonProcessingException e) {
//                // catch various errors
//                e.printStackTrace();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
// TODO https://github.com/wnameless/json-flattener

//            LOGGER.log(Level.INFO, "Activity: " + ((Activity) activity));
//            System.out.println(((Activity) activity).getComponents());
//            Component cmp = (Component) (ac.getContent().get(0).get("read date"));
//            System.out.println(cmp.getValue());
//        }
    }
}

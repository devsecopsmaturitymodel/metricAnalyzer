package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
@Configuration
public class YamlScanner {
    private static final Logger logger = LoggerFactory.getLogger(YamlScanner.class);

    //TODO How to make this path relative?
    @Value("${metricCA.skeleton.path}")
    public String yamlSkeletonFilePath;

    @Value("${metricCA.application.path}")
    public String yamlApplicationFolderPath;
    public Collection<File> getApplicationYamls() {
        // Fetch all files in yamlApplicationFolderPath
        File yamlApplicationFolder = new File(yamlApplicationFolderPath);
        return java.util.Arrays.asList(yamlApplicationFolder.listFiles());
    }

    public File getSkeletonYaml() throws FileNotFoundException {
        logger.info("yamlSekeletonFilePath" + yamlSkeletonFilePath);
        File skeletonConfig = new File(yamlSkeletonFilePath);
        if(!skeletonConfig.exists()) throw new FileNotFoundException(yamlSkeletonFilePath);
        return skeletonConfig;
    }
}

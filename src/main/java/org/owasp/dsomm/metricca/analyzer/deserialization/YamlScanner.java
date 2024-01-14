package org.owasp.dsomm.metricca.analyzer.deserialization;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class YamlScanner {
  private static final Logger logger = LoggerFactory.getLogger(YamlScanner.class);
  @Value("${metricCA.git.url}")
  private String yamlGitUrl;
  @Value("${metricCA.git.branch:master}")
  private String yamlGitBranch;
  @Value("${metricCA.git.targetPath:/tmp/metricCA/}")
  private String yamlGitTargetPath;
  @Value("${metricCA.skeleton.path}")
  private String yamlSkeletonFilePath;
  @Value("${metricCA.application.path}")
  private String yamlApplicationFolderPath;

  private static void deleteDirectoryRecursively(File dir) {
    File[] allContents = dir.listFiles();
    if (allContents != null) {
      for (File file : allContents) {
        deleteDirectoryRecursively(file);
      }
    }
    dir.delete();
  }

  public void initiate() throws GitAPIException {
    if (isGit()) {
      gitClone(false);
    }
  }

  public void initiateEnforced() throws GitAPIException {
    gitClone(true);
  }


  private void gitClone(boolean enforceGitCloneIfTargetFolderExists) throws GitAPIException {
    File yamlGitTargetPathFile = new File(yamlGitTargetPath);
    if (yamlGitTargetPathFile.exists()) {
      if (enforceGitCloneIfTargetFolderExists) {
        logger.info("yamlGitTargetPath exists, deleting it: " + yamlGitTargetPath + " enforceGitCloneIfTargetFolderExists:" + enforceGitCloneIfTargetFolderExists);
        deleteDirectoryRecursively(yamlGitTargetPathFile);
      } else {
        logger.info("yamlGitTargetPath exists, skipping cloning " + yamlGitTargetPath);
        return;
      }
    }
    if (yamlGitTargetPathFile.exists()) {
      logger.info("yamlGitTargetPath STILL exists");
    }
    logger.info("Cloning " + yamlGitUrl + " into " + yamlGitTargetPath);
    Git.cloneRepository()
        .setURI(yamlGitUrl)
        .setDirectory(yamlGitTargetPathFile)
        .setBranch(yamlGitBranch)
        .call();
  }

  public Collection<File> getApplicationYamls() throws IOException, GitAPIException {
    this.initiate();
    File yamlApplicationFolder = new File(getYamlApplicationFolderPath());
    if (!yamlApplicationFolder.exists()) {
      throw new FileNotFoundException(getYamlApplicationFolderPath());
    }

    try (Stream<Path> paths = Files.walk(Paths.get(yamlApplicationFolder.getAbsolutePath()))) {
      return paths
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".yaml") || path.toString().endsWith(".yml"))
          .map(Path::toFile)
          .collect(Collectors.toList());
    }
  }

  public File getSkeletonYaml() throws IOException, GitAPIException {
    this.initiate();
    logger.info("getYamlSkeletonFilePath() " + getYamlSkeletonFilePath());
    File skeletonConfig = new File(getYamlSkeletonFilePath());
    if (!skeletonConfig.exists()) throw new FileNotFoundException(getYamlSkeletonFilePath());

    return skeletonConfig;
  }

  private boolean isGit() {
    if (yamlGitUrl == null || yamlGitUrl.isEmpty()) {
      return false;
    }
    Path yamlGitTargetPath = Paths.get(this.yamlGitTargetPath);
    return Files.notExists(yamlGitTargetPath);
  }

  private String getYamlSkeletonFilePath() {
    if (isGit()) {
      return yamlGitTargetPath + "/" + yamlSkeletonFilePath;
    }
    return yamlSkeletonFilePath;
  }

  private String getYamlApplicationFolderPath() {
    logger.info("yamlApplicationFolderPath() " + yamlApplicationFolderPath);
    if (isGit()) {
      return yamlGitTargetPath + "/" + yamlApplicationFolderPath;
    }
    return yamlApplicationFolderPath;
  }
}

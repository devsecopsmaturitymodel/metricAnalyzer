package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
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
import java.util.List;
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
  @Value("${metricCA.teams.path}")
  private String yamlTeamsFilePath;

  @Value("${metricCA.application.path}")
  private String yamlApplicationFolderPath;
  @Value("${metricCA.git.usernameOrToken}")
  private String gitUsernameOrToken;
  @Value("${metricCA.git.password}")
  private String gitPassword;

  public void initiate() throws GitAPIException, IOException {
    if (isGit()) {
      gitCloneOrUpdate();
    }
  }

  private void gitCloneOrUpdate() throws GitAPIException, IOException {
    File yamlGitTargetPathFile = new File(yamlGitTargetPath);
    if (yamlGitTargetPathFile.exists()) {
      Git.open(new File(yamlGitTargetPath)).pull();
      logger.info("Pulled " + yamlGitUrl + " to " + yamlGitTargetPath);
      return;
    }

    CloneCommand repoCloneCommand = Git.cloneRepository()
        .setURI(yamlGitUrl)
        .setDirectory(yamlGitTargetPathFile)
        .setBranch(yamlGitBranch);

    if (gitUsernameOrToken != null && !gitUsernameOrToken.isEmpty()) {
      if (gitPassword == null || gitPassword.isEmpty()) {
        logger.info("Password is empty, assuming a token is used");
      } else {
        logger.debug("gitUsernameOrToken is set to " + gitUsernameOrToken);
      }
      CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitUsernameOrToken, gitPassword);
      repoCloneCommand.setCredentialsProvider(credentialsProvider);
    } else {
      logger.debug("gitUsernameOrToken is not set");
    }
    logger.info("Cloning " + yamlGitUrl + " into " + yamlGitTargetPath + " with branch " + yamlGitBranch);
    repoCloneCommand
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

  public List<Team> getTeamsAndApplicationYaml() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    JsonNode teamsNode = objectMapper.readTree(new File(yamlTeamsFilePath));
    String kind = teamsNode.get("kind").textValue();
    if (!kind.equals("teams")) {
      throw new RuntimeException("teams.yaml is not of kind teams (kind: " + kind + ")");
    }
    if (!teamsNode.has("teams")) {
      throw new RuntimeException("teams.yaml is missing the teams node");
    }
    String teamsString = objectMapper.writeValueAsString(teamsNode.get("teams"));
    ObjectMapper mapper = new ObjectMapper();
    logger.info("teamsNode.get(\"teams\") " + teamsString);
    List<Team> teams = objectMapper.readValue(teamsString, new TypeReference<List<Team>>() {
    });
    return teams;
  }

  private boolean isGitEnabled() {
    return yamlGitUrl != null && !yamlGitUrl.isEmpty();
  }

  private boolean isGit() {
    if (!isGitEnabled()) {
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

package org.owasp.dsomm.metricca.analyzer.deserialization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.deserialization.YamlScanner;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
public class YamlScannerTest {

  @TempDir
  private Path path;
  @InjectMocks
  private YamlScanner yamlScanner;

  @Test
  void shouldInitiateIsGitReturnFalse() {
    assertThatCode(() -> yamlScanner.initiate()).doesNotThrowAnyException();
  }

  @Test
  void shouldInitiate() {
    ReflectionTestUtils.setField(yamlScanner, "yamlGitUrl", "ab");
    ReflectionTestUtils.setField(yamlScanner, "yamlGitTargetPath", path.toString());

    assertThatCode(() -> yamlScanner.initiate()).doesNotThrowAnyException();
  }


}

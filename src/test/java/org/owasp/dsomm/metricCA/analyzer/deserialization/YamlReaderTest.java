package org.owasp.dsomm.metricca.analyzer.deserialization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.dsomm.metricca.analyzer.deserialization.YamlReader;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class YamlReaderTest {

  private static final String ERROR_MESSAGE = "No Map found in the YAML file.";

  @TempDir
  private Path path;
  @InjectMocks
  private YamlReader yamlReader;

  @Test
  void testConvertYamlToJavaYamlWhenYamlExist() throws FileNotFoundException {
    String filePath = ResourceUtils.getFile(Objects.requireNonNull(this.getClass().getResource("/skeleton.yaml")))
            .getPath();

    assertThat(YamlReader.convertYamlToJavaYaml(filePath)).isNotNull();
  }

  @Test
  void testConvertYamlToJavaYamlWhenYamlNotExist() {
    assertThat(YamlReader.convertYamlToJavaYaml(path.normalize().toString())).isNull();
  }

  @Test
  void testConvertYamlToJavaYamlForException() throws IOException {
    String filePath = ResourceUtils.getFile(Objects.requireNonNull(this.getClass().getResource("/test.yaml")))
            .getPath();

    assertThatThrownBy(() -> YamlReader.convertYamlToJavaYaml(filePath))
            .isInstanceOf(IllegalStateException.class)
            .extracting(IllegalStateException.class::cast)
            .returns(ERROR_MESSAGE, Throwable::getMessage);
  }
}

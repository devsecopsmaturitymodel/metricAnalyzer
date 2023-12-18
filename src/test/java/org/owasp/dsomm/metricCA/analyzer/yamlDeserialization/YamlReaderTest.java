package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class YamlReaderTest {

    private static final String ERROR_MESSAGE = "No Map found in the YAML file.";

    @TempDir
    private Path dir;
    @InjectMocks
    private YamlReader yamlReader;

    @Test
    void testConvertYamlToJavaYamlWhenYamlExist() {
        String filePath = "D://eiquanMetric//metricAnalyzer//src//main//resources//skeleton.yaml";

        assertThat(YamlReader.convertYamlToJavaYaml(filePath)).isNotNull();
    }

    @Test
    void testConvertYamlToJavaYamlWhenYamlNotExist() {
        String filePath = "E://eiquanMetric//metricAnalyzer//src//main//resources//test1.yaml";

        assertThat(YamlReader.convertYamlToJavaYaml(filePath)).isNull();
    }

    @Test
    void testConvertYamlToJavaYamlForException(){
        String filePath = "D://eiquanMetric//metricAnalyzer//src//main//resources//test.yaml";

        assertThatThrownBy(()-> YamlReader.convertYamlToJavaYaml(filePath))
                .isInstanceOf(IllegalStateException.class)
                .extracting(IllegalStateException.class::cast)
                .returns(ERROR_MESSAGE, Throwable::getMessage);
    }
}

package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class YamlScannerTest {

    @TempDir
    private Path dir;
    @InjectMocks
    private YamlScanner yamlScanner;

    @Test
    void shouldInitiateIsGitReturnFalse() throws GitAPIException, IOException {
        assertThatCode(() -> yamlScanner.initiate()).doesNotThrowAnyException();
    }

    @Test
    void shouldInitiate() throws GitAPIException, IOException {
        ReflectionTestUtils.setField(yamlScanner, "yamlGitUrl", "ab");
        ReflectionTestUtils.setField(yamlScanner, "yamlGitTargetPath", "D://eiquanMetric//metricAnalyzer");

        assertThatCode(() -> yamlScanner.initiate()).doesNotThrowAnyException();
    }


}

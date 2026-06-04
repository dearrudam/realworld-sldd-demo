package org.soujava.demo.sldd;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;

class BceArchitectureDocumentationTest {

    private static final Path PROJECT_ROOT = Path.of("").toAbsolutePath();
    private static final Path WORKSPACE_ROOT = PROJECT_ROOT.getParent();
    private static final Path BCE_BASELINE = PROJECT_ROOT.resolve("docs/bce-architecture.md");
    private static final Path SYSTEM_TEST_PROJECT = WORKSPACE_ROOT.resolve("realworld-api-st");

    @Test
    void bceBaselineDefinesProjectArchitecturePolicy() throws IOException {
        assertThat(BCE_BASELINE)
                .as("BCE architecture baseline document")
                .exists()
                .isRegularFile();

        assertThat(Files.readString(BCE_BASELINE))
                .contains(
                        "# RealWorld BCE Architecture Baseline",
                        "org.soujava.demo.sldd.<business-component>.<boundary|control|entity>",
                        "Generated starter resources are scaffold-only",
                        "JAX-RS resources belong in boundary packages",
                        "Control classes implement procedural business operations",
                        "Entity packages contain domain state and behavior",
                        "Cross-component collaboration uses explicit boundary or control entry points",
                        "realworld-api-st remains a black-box HTTP test harness");
    }

    @Test
    void systemTestsDoNotDependOnApiInternals() throws IOException {
        assertThat(Files.readString(SYSTEM_TEST_PROJECT.resolve("pom.xml")))
                .doesNotContain("<artifactId>realworld-api</artifactId>");

        assertThat(apiInternalImportsInSystemTests())
                .as("system-test sources must not import API implementation packages")
                .isEmpty();
    }

    @Test
    void workspaceGuidanceLinksTheBceBaseline() throws IOException {
        assertThat(Files.readString(WORKSPACE_ROOT.resolve("README.md")))
                .contains("realworld-api/docs/bce-architecture.md");

        assertThat(Files.readString(PROJECT_ROOT.resolve("README.md")))
                .contains("docs/bce-architecture.md");

        assertThat(Files.readString(WORKSPACE_ROOT.resolve("AGENTS.md")))
                .contains("realworld-api/docs/bce-architecture.md");

        assertThat(Files.readString(PROJECT_ROOT.resolve("AGENTS.md")))
                .contains("docs/bce-architecture.md");

        assertThat(Files.readString(SYSTEM_TEST_PROJECT.resolve("AGENTS.md")))
                .contains("../realworld-api/docs/bce-architecture.md");
    }

    private static String apiInternalImportsInSystemTests() throws IOException {
        try (var paths = Files.walk(SYSTEM_TEST_PROJECT.resolve("src"))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(BceArchitectureDocumentationTest::isJavaFile)
                    .map(BceArchitectureDocumentationTest::readSource)
                    .filter(BceArchitectureDocumentationTest::hasApiInternalImport)
                    .findFirst()
                    .orElse("");
        }
    }

    private static boolean isJavaFile(Path path) {
        return path.toString().endsWith(".java");
    }

    private static boolean hasApiInternalImport(String source) {
        return source.contains("import org.soujava.demo.sldd.");
    }

    private static String readSource(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read " + path, exception);
        }
    }
}

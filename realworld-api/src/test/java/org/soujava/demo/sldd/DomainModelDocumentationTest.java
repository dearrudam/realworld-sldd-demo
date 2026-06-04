package org.soujava.demo.sldd;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class DomainModelDocumentationTest {

    private static final Path PROJECT_ROOT = Path.of("").toAbsolutePath();
    private static final Path WORKSPACE_ROOT = PROJECT_ROOT.getParent();
    private static final Path DOMAIN_MODEL = PROJECT_ROOT.resolve("docs/domain-model.md");

    @Test
    void domainModelReferenceExists() {
        assertThat(DOMAIN_MODEL)
                .as("canonical RealWorld domain model reference")
                .exists()
                .isRegularFile();
    }

    @Test
    void domainModelReferenceContainsRequiredSections() throws IOException {
        var content = Files.readString(DOMAIN_MODEL);

        assertThat(content)
                .contains(
                        "# RealWorld Domain Model",
                        "## Model Version",
                        "## Source Evidence",
                        "## Entity Catalog",
                        "## Field Restrictions",
                        "## Relationship Cardinality",
                        "## Mermaid ER Diagram",
                        "## API Projections and Computed Fields",
                        "## Persistence Implications",
                        "## Evolution Policy");
    }

    @Test
    void domainModelReferenceCoversApprovedConcepts() throws IOException {
        var content = Files.readString(DOMAIN_MODEL);

        assertThat(content)
                .contains("User", "Profile", "Article", "Comment", "Tag", "Follow", "Favorite");
    }

    @Test
    void domainModelReferenceRecordsApprovedClassifications() throws IOException {
        var content = Files.readString(DOMAIN_MODEL);

        assertThat(content)
                .contains("Profile is an API projection")
                .contains("following is viewer-relative")
                .contains("favorited is viewer-relative")
                .contains("favoritesCount is computed");
    }

    @Test
    void workspaceDocumentationLinksDomainModel() throws IOException {
        var readme = Files.readString(WORKSPACE_ROOT.resolve("README.md"));

        assertThat(readme).contains("realworld-api/docs/domain-model.md");
    }

    @Test
    void agentRulesRequireDomainModelReference() throws IOException {
        var agentRules = Files.readString(PROJECT_ROOT.resolve("AGENTS.md"));

        assertThat(agentRules)
                .contains("docs/domain-model.md")
                .contains("entities")
                .contains("DTOs")
                .contains("repositories")
                .contains("validation rules")
                .contains("persistence mappings")
                .contains("relationship behavior");
    }
}

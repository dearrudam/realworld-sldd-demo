#!/usr/bin/env python3
from pathlib import Path
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
failures: list[str] = []


def require(condition: bool, message: str) -> None:
    if not condition:
        failures.append(message)


def read(path: str) -> str:
    file = ROOT / path
    require(file.exists(), f"{path} exists")
    return file.read_text(encoding="utf-8") if file.exists() else ""


def dependency_artifacts(pom_path: str) -> list[tuple[str, str]]:
    pom = ROOT / pom_path
    require(pom.exists(), f"{pom_path} exists")
    if not pom.exists():
        return []
    tree = ET.parse(pom)
    namespace = "{http://maven.apache.org/POM/4.0.0}"
    artifacts: list[tuple[str, str]] = []
    for dependency in tree.getroot().findall(f".//{namespace}dependencies/{namespace}dependency"):
        group_id = dependency.findtext(f"{namespace}groupId", default="")
        artifact_id = dependency.findtext(f"{namespace}artifactId", default="")
        artifacts.append((group_id, artifact_id))
    return artifacts


for directory in ("realworld-api", "realworld-api-st"):
    require((ROOT / directory).is_dir(), f"{directory}/ directory exists")

for file_path in (
    "realworld-api/pom.xml",
    "realworld-api-st/pom.xml",
    "AGENTS.md",
    "realworld-api/AGENTS.md",
    "realworld-api-st/AGENTS.md",
):
    require((ROOT / file_path).is_file(), f"{file_path} exists")

api_dependencies = dependency_artifacts("realworld-api/pom.xml")
st_dependencies = dependency_artifacts("realworld-api-st/pom.xml")
api_artifacts = {artifact_id for _, artifact_id in api_dependencies}
st_dependencies_set = set(st_dependencies)
st_artifacts = {artifact_id for _, artifact_id in st_dependencies}

require("quarkus-rest-jsonb" in api_artifacts, "realworld-api declares quarkus-rest-jsonb")
require("quarkus-jnosql-mongodb" in api_artifacts, "realworld-api declares quarkus-jnosql-mongodb")
require("quarkus-rest-client" in st_artifacts, "realworld-api-st declares quarkus-rest-client")
require(("org.soujava.demo.sldd", "realworld-api") not in st_dependencies_set, "realworld-api-st has no dependency on realworld-api")

readme = read("README.md")
require("[YOUR_FRAMEWORK]" not in readme, "README removes [YOUR_FRAMEWORK] placeholders")
for expected in ("Quarkus", "SLDD", "realworld-api", "realworld-api-st"):
    require(expected in readme, f"README mentions {expected}")

gitignore = read(".gitignore")
for expected in ("target/", ".mvn/wrapper/maven-wrapper.jar", "*.log", ".quarkus/"):
    require(expected in gitignore, f".gitignore includes {expected}")

if failures:
    print("Scaffold verification failed:")
    for failure in failures:
        print(f"- {failure}")
    sys.exit(1)

print("Scaffold verification passed.")

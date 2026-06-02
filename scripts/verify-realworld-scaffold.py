#!/usr/bin/env python3
"""Verify the initial RealWorld Quarkus + SLDD scaffold."""
from __future__ import annotations

import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
NS = {"m": "http://maven.apache.org/POM/4.0.0"}
failures: list[str] = []


def require(condition: bool, message: str) -> None:
    if not condition:
        failures.append(message)


def text(path: Path) -> str:
    try:
        return path.read_text(encoding="utf-8")
    except FileNotFoundError:
        failures.append(f"Missing required file: {path.relative_to(ROOT)}")
        return ""


def parse_pom(path: Path) -> ET.Element | None:
    if not path.exists():
        failures.append(f"Missing required Maven project file: {path.relative_to(ROOT)}")
        return None
    try:
        return ET.parse(path).getroot()
    except ET.ParseError as exc:
        failures.append(f"Invalid Maven POM XML in {path.relative_to(ROOT)}: {exc}")
        return None


def direct_dependencies(pom: ET.Element | None) -> set[tuple[str, str]]:
    if pom is None:
        return set()
    deps: set[tuple[str, str]] = set()
    for dep in pom.findall("m:dependencies/m:dependency", NS):
        group_id = dep.findtext("m:groupId", default="", namespaces=NS).strip()
        artifact_id = dep.findtext("m:artifactId", default="", namespaces=NS).strip()
        deps.add((group_id, artifact_id))
    return deps


api_pom = ROOT / "realworld-api" / "pom.xml"
st_pom = ROOT / "realworld-api-st" / "pom.xml"

require(api_pom.exists(), "realworld-api/pom.xml must exist")
require(st_pom.exists(), "realworld-api-st/pom.xml must exist")
require((ROOT / "AGENTS.md").exists(), "root AGENTS.md must exist")
require((ROOT / "realworld-api" / "AGENTS.md").exists(), "realworld-api/AGENTS.md must exist")
require((ROOT / "realworld-api-st" / "AGENTS.md").exists(), "realworld-api-st/AGENTS.md must exist")

api_deps = direct_dependencies(parse_pom(api_pom))
st_deps = direct_dependencies(parse_pom(st_pom))

require(
    ("io.quarkus", "quarkus-rest-jsonb") in api_deps,
    "realworld-api must declare io.quarkus:quarkus-rest-jsonb",
)
require(
    ("io.quarkiverse.jnosql", "quarkus-jnosql-mongodb") in api_deps,
    "realworld-api must declare io.quarkiverse.jnosql:quarkus-jnosql-mongodb",
)
require(
    ("io.quarkus", "quarkus-rest-client") in st_deps,
    "realworld-api-st must declare io.quarkus:quarkus-rest-client",
)
require(
    not any(group == "org.soujava.demo.sldd" and artifact == "realworld-api" for group, artifact in st_deps),
    "realworld-api-st must not depend on realworld-api internals",
)

readme = text(ROOT / "README.md")
require("[YOUR_FRAMEWORK]" not in readme, "README.md must not contain [YOUR_FRAMEWORK] placeholders")
for token in ("Quarkus", "SLDD", "realworld-api", "realworld-api-st"):
    require(token in readme, f"README.md must describe {token}")

gitignore = text(ROOT / ".gitignore")
for pattern in ("target/", ".mvn/wrapper/maven-wrapper.jar", ".quarkus/", "*.log"):
    require(pattern in gitignore, f".gitignore must include {pattern}")

if failures:
    print("RealWorld scaffold verification failed:")
    for failure in failures:
        print(f"- {failure}")
    sys.exit(1)

print("RealWorld scaffold verification passed.")

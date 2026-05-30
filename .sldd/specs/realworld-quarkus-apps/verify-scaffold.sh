#!/usr/bin/env bash
set -u

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../../.." && pwd)"
failures=0

fail() {
  printf 'FAIL: %s\n' "$1"
  failures=$((failures + 1))
}

pass() {
  printf 'PASS: %s\n' "$1"
}

require_file() {
  local file="$1"
  local label="$2"

  if [[ -f "${ROOT_DIR}/${file}" ]]; then
    pass "${label} exists"
  else
    fail "${label} is missing: ${file}"
  fi
}

require_dir() {
  local dir="$1"
  local label="$2"

  if [[ -d "${ROOT_DIR}/${dir}" ]]; then
    pass "${label} exists"
  else
    fail "${label} is missing: ${dir}"
  fi
}

require_file_contains() {
  local file="$1"
  local pattern="$2"
  local label="$3"

  if [[ ! -f "${ROOT_DIR}/${file}" ]]; then
    fail "${label} cannot be checked because ${file} is missing"
    return
  fi

  if grep -Eq -- "${pattern}" "${ROOT_DIR}/${file}"; then
    pass "${label}"
  else
    fail "${label}"
  fi
}

require_file_not_contains() {
  local file="$1"
  local pattern="$2"
  local label="$3"

  if [[ ! -f "${ROOT_DIR}/${file}" ]]; then
    fail "${label} cannot be checked because ${file} is missing"
    return
  fi

  if grep -Eq -- "${pattern}" "${ROOT_DIR}/${file}"; then
    fail "${label}"
  else
    pass "${label}"
  fi
}

require_no_file_contains() {
  local file="$1"
  local pattern="$2"
  local label="$3"

  if [[ ! -f "${ROOT_DIR}/${file}" ]]; then
    fail "${label} cannot be checked because ${file} is missing"
    return
  fi

  if grep -Eq -- "${pattern}" "${ROOT_DIR}/${file}"; then
    fail "${label}"
  else
    pass "${label}"
  fi
}

require_dir "realworld-api" "API project directory"
require_file "realworld-api/pom.xml" "API Maven project"
require_file "realworld-api/AGENTS.md" "API project guidance"
require_file_contains "realworld-api/pom.xml" "quarkus-rest-jsonb" "API declares Quarkus REST JSON-B"
require_file_contains "realworld-api/pom.xml" "quarkus-jnosql-mongodb|jnosql-mongodb" "API declares JNoSQL MongoDB"

require_dir "realworld-api-st" "System-test project directory"
require_file "realworld-api-st/pom.xml" "System-test Maven project"
require_file "realworld-api-st/AGENTS.md" "System-test project guidance"
require_file_contains "realworld-api-st/pom.xml" "quarkus-rest-client" "System-test app declares Quarkus REST Client"
require_no_file_contains "realworld-api-st/pom.xml" "<artifactId>realworld-api</artifactId>" "System-test app does not depend on API internals"

require_file "AGENTS.md" "Root workspace guidance"
require_file_not_contains "README.md" "\\[YOUR_FRAMEWORK\\]" "README removes RealWorld template placeholders"
require_file_contains "README.md" "Quarkus" "README documents Quarkus"
require_file_contains "README.md" "SLDD" "README documents SLDD"
require_file_contains "README.md" "realworld-api" "README documents realworld-api"
require_file_contains "README.md" "realworld-api-st" "README documents realworld-api-st"

require_file_contains ".gitignore" "(^|/)target/|target/" ".gitignore excludes Maven target outputs"
require_file_contains ".gitignore" "\\.mvn/wrapper/maven-wrapper\\.jar" ".gitignore excludes Maven wrapper jar"
require_file_contains ".gitignore" "\\.quarkus/" ".gitignore excludes Quarkus generated state"

if (( failures > 0 )); then
  printf '\nScaffold verification failed with %d issue(s).\n' "${failures}"
  exit 1
fi

printf '\nScaffold verification passed.\n'

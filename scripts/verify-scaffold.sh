#!/usr/bin/env sh
set -eu

failures=0

check_file() {
  if [ ! -f "$1" ]; then
    printf 'FAIL missing file: %s\n' "$1"
    failures=$((failures + 1))
  else
    printf 'PASS file exists: %s\n' "$1"
  fi
}

check_contains() {
  file="$1"
  pattern="$2"
  description="$3"

  if [ ! -f "$file" ]; then
    printf 'FAIL %s: %s is missing\n' "$description" "$file"
    failures=$((failures + 1))
  elif ! grep -Eq "$pattern" "$file"; then
    printf 'FAIL %s: pattern not found in %s\n' "$description" "$file"
    failures=$((failures + 1))
  else
    printf 'PASS %s\n' "$description"
  fi
}

check_not_contains() {
  file="$1"
  pattern="$2"
  description="$3"

  if [ ! -f "$file" ]; then
    printf 'FAIL %s: %s is missing\n' "$description" "$file"
    failures=$((failures + 1))
  elif grep -Eq "$pattern" "$file"; then
    printf 'FAIL %s: forbidden pattern found in %s\n' "$description" "$file"
    failures=$((failures + 1))
  else
    printf 'PASS %s\n' "$description"
  fi
}

check_file "realworld-api/pom.xml"
check_file "realworld-api-st/pom.xml"
check_file "AGENTS.md"
check_file "realworld-api/AGENTS.md"
check_file "realworld-api-st/AGENTS.md"

check_contains "realworld-api/pom.xml" "quarkus-rest-jsonb" "realworld-api includes Quarkus REST JSON-B"
check_contains "realworld-api/pom.xml" "quarkus-jnosql-mongodb|jnosql-mongodb" "realworld-api includes JNoSQL MongoDB"
check_contains "realworld-api-st/pom.xml" "quarkus-rest-client" "realworld-api-st includes Quarkus REST Client"
check_not_contains "realworld-api-st/pom.xml" "<artifactId>realworld-api</artifactId>" "realworld-api-st has no Maven dependency on realworld-api"

check_not_contains "README.md" "\[YOUR_FRAMEWORK\]" "README removes template framework placeholders"
check_contains "README.md" "Quarkus" "README describes Quarkus"
check_contains "README.md" "SLDD" "README describes SLDD"
check_contains "README.md" "realworld-api" "README documents realworld-api"
check_contains "README.md" "realworld-api-st" "README documents realworld-api-st"

check_contains ".gitignore" '(^|/)target/' ".gitignore excludes Maven target directories"
check_contains ".gitignore" '(^|/)\.quarkus/' ".gitignore excludes Quarkus dev output"
check_contains ".gitignore" '(^|/)\.mvn/wrapper/maven-wrapper.jar' ".gitignore excludes Maven wrapper jar"

if [ "$failures" -gt 0 ]; then
  printf '\nScaffold verification failed with %s failure(s).\n' "$failures"
  exit 1
fi

printf '\nScaffold verification passed.\n'

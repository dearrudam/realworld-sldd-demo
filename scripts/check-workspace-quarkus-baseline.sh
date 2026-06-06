#!/usr/bin/env bash
set -euo pipefail

fail() {
  echo "workspace baseline check failed: $*" >&2
  exit 1
}

require_file() {
  [[ -f "$1" ]] || fail "missing file: $1"
}

require_dir() {
  [[ -d "$1" ]] || fail "missing directory: $1"
}

require_text() {
  local file="$1"
  local text="$2"
  grep -Fq -- "$text" "$file" || fail "missing required text in $file: $text"
}

baseline=".sldd/specs/workspace-quarkus-baseline/workspace-quarkus-baseline.md"
contract=".sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md"

require_file "$contract"
require_file "$baseline"

for app in realworld-api realworld-api-st; do
  require_dir "$app"
  require_file "$app/pom.xml"
  require_file "$app/mvnw"
  require_dir "$app/src/main/java"
  require_dir "$app/src/main/resources"
  require_dir "$app/src/test/java"
  require_file "$app/src/main/resources/application.properties"
done

require_text "$baseline" "# Quarkus Workspace Baseline"
require_text "$baseline" "realworld-api"
require_text "$baseline" "realworld-api-st"
require_text "$baseline" "./mvnw quarkus:dev"
require_text "$baseline" "./mvnw test"
require_text "$baseline" "./mvnw install"
require_text "$baseline" "8080"
require_text "$baseline" "8081"
require_text "$baseline" "http://localhost:8080"
require_text "$baseline" "realworld-api-contract-baseline.md"
require_text "$baseline" "No RealWorld business endpoint behavior is implemented by this workflow."

require_text "realworld-api/src/main/resources/application.properties" "quarkus.http.port=8080"
require_text "realworld-api-st/src/main/resources/application.properties" "quarkus.http.port=8081"
require_text "realworld-api-st/src/main/resources/application.properties" "realworld-api.base-url=http://localhost:8080"

echo "workspace baseline check passed"

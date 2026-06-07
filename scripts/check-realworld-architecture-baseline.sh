#!/usr/bin/env bash
set -euo pipefail

fail() {
  echo "architecture baseline check failed: $*" >&2
  exit 1
}

require_file() {
  [[ -f "$1" ]] || fail "missing file: $1"
}

require_text() {
  local file="$1"
  local text="$2"
  grep -Fq -- "$text" "$file" || fail "missing required text in $file: $text"
}

baseline=".sldd/specs/realworld-architecture-baseline/realworld-architecture-baseline.md"
readme="README.md"

require_file "$baseline"
require_file "$readme"

require_text "$baseline" "# RealWorld API Architecture Baseline"
require_text "$baseline" 'dev.realworld.<business-component>.<boundary|control|entity>'
require_text "$baseline" "Boundary classes adapt HTTP and JSON envelopes"
require_text "$baseline" "Control classes contain procedural business logic"
require_text "$baseline" "Entity classes contain domain state and behavior"
require_text "$baseline" "MongoDB"
require_text "$baseline" "quarkus-jnosql-mongodb"
require_text "$baseline" "JSON-B"
require_text "$baseline" "quarkus-rest-jsonb"
require_text "$baseline" "JWT"
require_text "$baseline" "quarkus-smallrye-jwt"
require_text "$baseline" "Authorization: Bearer <token>"
require_text "$baseline" "profile-scoped Quarkus configuration"
require_text "$baseline" "standalone HTTP system tests"
require_text "$baseline" "realworld-api-st calls realworld-api over HTTP"
require_text "$baseline" "No RealWorld endpoint behavior is implemented by this baseline"

require_text "$readme" ".sldd/specs/realworld-architecture-baseline/realworld-architecture-baseline.md"
require_text "$readme" "./scripts/check-realworld-architecture-baseline.sh"

echo "architecture baseline check passed"

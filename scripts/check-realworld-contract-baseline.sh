#!/usr/bin/env bash
set -euo pipefail

contract=".sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md"

fail() {
  echo "contract baseline check failed: $*" >&2
  exit 1
}

[[ -f "$contract" ]] || fail "missing $contract"

require_text() {
  local text="$1"
  grep -Fq -- "$text" "$contract" || fail "missing required text: $text"
}

require_text "# RealWorld API Contract Baseline"
require_text "## Endpoint Inventory"
require_text "## Authentication Convention"
require_text "## JSON Envelope Convention"
require_text "## Error Convention"
require_text "## Acceptance Boundaries"

require_text "POST /api/users"
require_text "POST /api/users/login"
require_text "GET /api/user"
require_text "PUT /api/user"
require_text "GET /api/profiles/{username}"
require_text "POST /api/profiles/{username}/follow"
require_text "DELETE /api/profiles/{username}/follow"
require_text "GET /api/articles"
require_text "GET /api/articles/feed"
require_text "POST /api/articles"
require_text "GET /api/articles/{slug}"
require_text "PUT /api/articles/{slug}"
require_text "DELETE /api/articles/{slug}"
require_text "POST /api/articles/{slug}/favorite"
require_text "DELETE /api/articles/{slug}/favorite"
require_text "GET /api/articles/{slug}/comments"
require_text "POST /api/articles/{slug}/comments"
require_text "DELETE /api/articles/{slug}/comments/{id}"
require_text "GET /api/tags"

require_text "Authorization: Bearer <token>"
require_text "JWT"
require_text "errors"
require_text "422"
require_text "401"
require_text "403"
require_text "404"
require_text "This workflow does not create Quarkus modules or production endpoint implementation."

echo "contract baseline check passed"

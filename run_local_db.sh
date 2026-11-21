#!/usr/bin/env bash
set -euo pipefail

BROWSER="${1:-chrome}"
ENVIRONMENT="${2:-local}"
TOGGLE_SCRIPT="./toggle-feature-switch-db-sm2.sh"

cleanup() {
  echo "Restoring original backend config..."
  "$TOGGLE_SCRIPT" restore || true
}

trap cleanup EXIT

echo "Switching backend to database mode..."
"$TOGGLE_SCRIPT" set-db

echo "Running format checks and compile..."
sbt --color=true scalafmtAll scalafmtCheckAll scalafmtSbtCheck clean compile

echo "Running tests against database only..."
sbt --color=true \
  -Dbrowser="$BROWSER" \
  -Denvironment="$ENVIRONMENT" \
  -Dbrowser.option.headless=true \
  -DuseStub=false \
  "testOnly uk.gov.hmrc.ui.specs* -- -n uk.gov.hmrc.ui.tags.Local" \
  testReport

echo "Done."
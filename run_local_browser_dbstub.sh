#!/usr/bin/env bash
set -euo pipefail

BROWSER="${1:-chrome}"
ENVIRONMENT="${2:-local}"
TOGGLE_SCRIPT="./toggle-feature-switch-sm2.sh"

cleanup() {
  echo "Resetting backend to stub..."
  "$TOGGLE_SCRIPT" stub
}

trap cleanup EXIT

echo "Ensuring backend starts on stub..."
"$TOGGLE_SCRIPT" stub

echo "Running format checks and compile..."
sbt scalafmtAll scalafmtCheckAll scalafmtSbtCheck clean compile

echo "Running tests against stub..."
sbt \
  -Dbrowser="$BROWSER" \
  -Denvironment="$ENVIRONMENT" \
  -Dbrowser.option.headless=false \
  -Dreport.dir="target/test-reports-stub" \
  "testOnly uk.gov.hmrc.ui.specs* -- -n uk.gov.hmrc.ui.tags.Local" \
  testReport

echo "Switching backend to database..."
"$TOGGLE_SCRIPT" db

echo "Running tests against database..."
sbt \
  -Dbrowser="$BROWSER" \
  -Denvironment="$ENVIRONMENT" \
  -Dbrowser.option.headless=false \
  -DuseStub=false \
  -Dreport.dir="target/test-reports-db" \
  "testOnly uk.gov.hmrc.ui.specs* -- -n uk.gov.hmrc.ui.tags.Local" \
  testReport

echo "Done."
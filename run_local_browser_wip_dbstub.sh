#!/usr/bin/env bash
set -euo pipefail

BROWSER="${1:-chrome}"
ENVIRONMENT="${2:-local}"
TOGGLE_SCRIPT="./toggle-feature-switch-sm2.sh"

cleanup() {
  echo "Resetting backend to stub..."
  "$TOGGLE_SCRIPT" stub || true
}

trap cleanup EXIT

run_tests() {
  local mode="$1"
  local use_stub="$2"
  local report_dir="$3"

  echo "Running tests against $mode..."
  sbt \
    -Dbrowser="$BROWSER" \
    -Denvironment="$ENVIRONMENT" \
    -Dbrowser.option.headless=false \
    -DuseStub="$use_stub" \
    -Dreport.dir="$report_dir" \
    "testOnly uk.gov.hmrc.ui.specs* -- -n uk.gov.hmrc.ui.tags.wip"
}

run_report() {
  local mode="$1"
  local report_dir="$2"

  echo "Generating accessibility report for $mode..."
  sbt \
    -Dreport.dir="$report_dir" \
    testReport || {
      echo "WARNING: testReport failed for $mode, continuing..."
      return 0
    }
}

echo "Ensuring backend starts on stub..."
"$TOGGLE_SCRIPT" stub

echo "Running format checks and compile..."
sbt scalafmtAll scalafmtCheckAll scalafmtSbtCheck clean compile

run_tests "stub" true "target/test-reports-stub"
run_report "stub" "target/test-reports-stub"

echo "Switching backend to database..."
"$TOGGLE_SCRIPT" db

run_tests "database" false "target/test-reports-db"
run_report "database" "target/test-reports-db"

echo "Done."
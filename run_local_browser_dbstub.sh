#!/usr/bin/env bash
set -euo pipefail

BROWSER="${1:-chrome}"
ENVIRONMENT="${2:-local}"
TOGGLE_SCRIPT="./toggle-feature-switch-sm2.sh"

INFO_COLOR=$'\033[1;34m'
WARN_COLOR=$'\033[1;33m'
SUCCESS_COLOR=$'\033[1;32m'
RESET_COLOR=$'\033[0m'

STUB_TEST_LOG="$(mktemp)"
DB_TEST_LOG="$(mktemp)"
STUB_REPORT_LOG="$(mktemp)"
DB_REPORT_LOG="$(mktemp)"

cleanup() {
  echo "Resetting backend to stub..."
  "$TOGGLE_SCRIPT" stub || true
  rm -f "$STUB_TEST_LOG" "$DB_TEST_LOG" "$STUB_REPORT_LOG" "$DB_REPORT_LOG"
}

trap cleanup EXIT

strip_ansi() {
  sed -r 's/\x1B\[[0-9;]*[mK]//g' "$1"
}

extract_summary() {
  strip_ansi "$1" | awk '
    /^\[info\] Run completed/ ||
    /^\[info\] Total number of tests run:/ ||
    /^\[info\] Suites:/ ||
    /^\[info\] Tests:/ ||
    /^\[info\] All tests passed\./ ||
    /^\[success\] Total time:/ {
      print
    }
  '
}

extract_report_summary() {
  strip_ansi "$1" | awk '
    /Generating accessibility report/ ||
    /Accessibility assessment:/ ||
    /Wrote accessibility assessment report/ {
      print
    }
  '
}

run_tests() {
  local mode="$1"
  local use_stub="$2"
  local report_dir="$3"
  local log_file="$4"

  echo "Running tests against $mode..."

  sbt --color=true \
    -Dbrowser="$BROWSER" \
    -Denvironment="$ENVIRONMENT" \
    -Dbrowser.option.headless=false \
    -DuseStub="$use_stub" \
    -Dreport.dir="$report_dir" \
    "testOnly uk.gov.hmrc.ui.specs* -- -n uk.gov.hmrc.ui.tags.Local" \
    2>&1 | tee "$log_file"
}

run_report() {
  local mode="$1"
  local report_dir="$2"
  local log_file="$3"

  {
    echo "Generating accessibility report for $mode..."
    sbt --color=true -Dreport.dir="$report_dir" testReport
  } 2>&1 | tee "$log_file"
}

echo "Ensuring backend starts on stub..."
"$TOGGLE_SCRIPT" stub

echo "Running format checks and compile..."
sbt --color=true scalafmtAll scalafmtCheckAll scalafmtSbtCheck clean compile

run_tests "stub" true "target/test-reports-stub" "$STUB_TEST_LOG"
run_report "stub" "target/test-reports-stub" "$STUB_REPORT_LOG"

echo "Switching backend to database..."
"$TOGGLE_SCRIPT" db

run_tests "database" false "target/test-reports-db" "$DB_TEST_LOG"
run_report "database" "target/test-reports-db" "$DB_REPORT_LOG"

print_colored_summary() {
  while IFS= read -r line; do
    case "$line" in
      "[info]"*)
        printf '%s%s%s\n' "$INFO_COLOR" "$line" "$RESET_COLOR"
        ;;
      "[warn]"*)
        printf '%s%s%s\n' "$WARN_COLOR" "$line" "$RESET_COLOR"
        ;;
      "[success]"*)
        printf '%s%s%s\n' "$SUCCESS_COLOR" "$line" "$RESET_COLOR"
        ;;
      *)
        printf '%s\n' "$line"
        ;;
    esac
  done
}


echo
echo "================ Stub test summary: ================"
echo
extract_summary "$STUB_TEST_LOG" | print_colored_summary
echo
echo "================ Database test summary: ================"
echo
extract_summary "$DB_TEST_LOG" | print_colored_summary
echo
echo "================ Accessibility summary ================"
echo
extract_report_summary "$STUB_REPORT_LOG" | print_colored_summary
echo
extract_report_summary "$DB_REPORT_LOG" | print_colored_summary
echo
echo "Done."
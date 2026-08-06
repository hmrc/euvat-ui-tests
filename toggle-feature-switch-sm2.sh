#!/usr/bin/env bash
set -euo pipefail
shopt -s nullglob

LOCAL_FILE="$HOME/workspace/euvat-refunds/conf/application.conf"
SM2_MATCHES=( "$HOME"/.sm2/install/euvat-refunds/euvat-refunds-*/conf/application.conf )

usage() {
  echo "Usage: $0 stub|db"
  exit 1
}

[ $# -eq 1 ] || usage

case "$1" in
  stub)
    new="true"
    message="you are now using the stub"
    ;;
  db)
    new="false"
    message="you are now connected to the database"
    ;;
  *)
    usage
    ;;
esac

set_switches() {
  local file="$1"
  [ -f "$file" ] || { echo "Config file not found: $file"; exit 1; }

  sed -i -E "s/^([[:space:]]*rds-cande-stubbed[[:space:]]*=[[:space:]]*).*/\1$new/" "$file"
  sed -i -E "s/^([[:space:]]*rds-datacache-stubbed[[:space:]]*=[[:space:]]*).*/\1$new/" "$file"
}

is_sm2_euvat_running() {
  pgrep -af "euvat-refunds" | grep -q "/.sm2/install/euvat-refunds/"
}

is_local_euvat_running() {
  pgrep -af "euvat-refunds" | grep -q "$HOME/workspace/euvat-refunds"
}

if is_sm2_euvat_running; then
  if (( ${#SM2_MATCHES[@]} == 0 )); then
    echo "EUVAT_REFUNDS appears to be running in sm2, but no sm2 config was found"
    exit 1
  fi

  if (( ${#SM2_MATCHES[@]} > 1 )); then
    echo "Multiple sm2 application.conf files found:"
    printf '%s\n' "${SM2_MATCHES[@]}"
    exit 1
  fi

  FILE="${SM2_MATCHES[0]}"

  echo "Detected EUVAT_REFUNDS running from sm2"
  echo "Stopping EUVAT_REFUNDS..."
  sm2 --stop EUVAT_REFUNDS || true

  echo "Updating $FILE..."
  set_switches "$FILE"

  echo "Starting EUVAT_REFUNDS..."
  sm2 --start EUVAT_REFUNDS

elif is_local_euvat_running || [ -f "$LOCAL_FILE" ]; then
  FILE="$LOCAL_FILE"
  echo "Detected local workspace run"
  echo "Updating $FILE..."
  set_switches "$FILE"

else
  echo "Could not determine whether euvat-refunds is running from sm2 or workspace"
  exit 1
fi

echo "Updated: $FILE"
echo "Set feature switches to $new - $message"
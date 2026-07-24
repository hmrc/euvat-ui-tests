#!/usr/bin/env bash
set -euo pipefail
shopt -s nullglob

matches=( "$HOME"/.sm2/install/euvat-refunds/euvat-refunds-*/conf/application.conf )

if (( ${#matches[@]} == 0 )); then
  echo "No sm2 application.conf found"
  exit 1
fi

if (( ${#matches[@]} > 1 )); then
  echo "Multiple sm2 application.conf files found:"
  printf '%s\n' "${matches[@]}"
  echo "Please remove old versions or update the script to choose one."
  exit 1
fi

FILE="${matches[0]}"

[ -f "$FILE" ] || { echo "Config file not found: $FILE"; exit 1; }

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

echo "Stopping EUVAT_REFUNDS..."
sm2 --stop EUVAT_REFUNDS

echo "Updating $FILE..."
sed -i -E "s/^([[:space:]]*rds-cande-stubbed[[:space:]]*=[[:space:]]*).*/\1$new/" "$FILE"
sed -i -E "s/^([[:space:]]*rds-datacache-stubbed[[:space:]]*=[[:space:]]*).*/\1$new/" "$FILE"

echo "Starting EUVAT_REFUNDS..."
sm2 --start EUVAT_REFUNDS

echo "Updated: $FILE"
echo "Set feature switches to $new - $message"
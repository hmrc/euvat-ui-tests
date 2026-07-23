#!/usr/bin/env bash
set -euo pipefail

FILE="$HOME/workspace/euvat-refunds/conf/application.conf"
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

sed -i -E "s/^([[:space:]]*rds-cande-stubbed[[:space:]]*=[[:space:]]*).*/\1$new/" "$FILE"
sed -i -E "s/^([[:space:]]*rds-datacache-stubbed[[:space:]]*=[[:space:]]*).*/\1$new/" "$FILE"

echo "Set feature switches to $new - $message"
#!/usr/bin/env bash
set -euo pipefail
shopt -s nullglob

LOCAL_FILE="$HOME/workspace/euvat-refunds/conf/application.conf"
SM2_MATCHES=( "$HOME"/.sm2/install/euvat-refunds/euvat-refunds-*/conf/application.conf )

usage() {
  echo "Usage: $0 set-db|restore"
  exit 1
}

[ $# -eq 1 ] || usage

ACTION="$1"
STATE_FILE="${TMPDIR:-/tmp}/euvat_refunds_switch_state"

set_switches() {
  local file="$1"
  local rds_cande="$2"
  local rds_datacache="$3"

  [ -f "$file" ] || { echo "Config file not found: $file"; exit 1; }

  sed -i -E "s/^([[:space:]]*rds-cande-stubbed[[:space:]]*=[[:space:]]*).*/\1$rds_cande/" "$file"
  sed -i -E "s/^([[:space:]]*rds-datacache-stubbed[[:space:]]*=[[:space:]]*).*/\1$rds_datacache/" "$file"
}

get_current_value() {
  local file="$1"
  local key="$2"
  grep -E "^[[:space:]]*$key[[:space:]]*=" "$file" | sed -E 's/.*=[[:space:]]*//' | tail -1
}

is_sm2_euvat_running() {
  pgrep -af "euvat-refunds" | grep -q "/.sm2/install/euvat-refunds/"
}

is_local_euvat_running() {
  pgrep -af "euvat-refunds" | grep -q "$HOME/workspace/euvat-refunds"
}

resolve_file() {
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
    MODE="sm2"
  elif is_local_euvat_running || [ -f "$LOCAL_FILE" ]; then
    FILE="$LOCAL_FILE"
    MODE="local"
  else
    echo "Could not determine whether euvat-refunds is running from sm2 or workspace"
    exit 1
  fi
}

restart_if_needed() {
  if [ "$MODE" = "sm2" ]; then
    echo "Stopping EUVAT_REFUNDS..."
    sm2 --stop EUVAT_REFUNDS || true
    echo "Starting EUVAT_REFUNDS..."
    sm2 --start EUVAT_REFUNDS
  fi
}

resolve_file

case "$ACTION" in
  set-db)
    orig_cande="$(get_current_value "$FILE" "rds-cande-stubbed")"
    orig_datacache="$(get_current_value "$FILE" "rds-datacache-stubbed")"

    cat > "$STATE_FILE" <<EOF
FILE=$FILE
MODE=$MODE
RDS_CANDE=$orig_cande
RDS_DATACACHE=$orig_datacache
EOF

    echo "Switching services to database mode..."
    [ "$MODE" = "sm2" ] && sm2 --stop EUVAT_REFUNDS || true
    set_switches "$FILE" "false" "false"
    [ "$MODE" = "sm2" ] && sm2 --start EUVAT_REFUNDS

    echo "Updated: $FILE"
    echo "Database mode enabled"
    ;;
  restore)
    [ -f "$STATE_FILE" ] || { echo "No state file found: nothing to restore"; exit 1; }

    # shellcheck disable=SC1090
    source "$STATE_FILE"

    echo "Restoring original config..."
    [ "$MODE" = "sm2" ] && sm2 --stop EUVAT_REFUNDS || true
    set_switches "$FILE" "$RDS_CANDE" "$RDS_DATACACHE"
    [ "$MODE" = "sm2" ] && sm2 --start EUVAT_REFUNDS

    rm -f "$STATE_FILE"

    echo "Updated: $FILE"
    echo "Original config restored"
    ;;
  *)
    usage
    ;;
esac
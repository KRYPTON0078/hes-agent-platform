#!/usr/bin/env bash
set -euo pipefail
BASE=${1:-http://localhost:8080}
DEVICE_ID=${2:-HES-SMOKE-001}
curl -s "$BASE/api/v1/ping" | tee /tmp/hes-ping.json
curl -s -X POST "$BASE/api/v1/agent/messages" \
  -H 'Content-Type: application/json' \
  -d "{\"protocolVersion\":\"1.0\",\"type\":\"AGENT_REGISTER\",\"messageId\":\"$(uuidgen)\",\"deviceId\":\"$DEVICE_ID\",\"payload\":{\"model\":\"HES-BAT-10K\",\"firmwareVersion\":\"1.0.0\"}}"
curl -s "$BASE/api/v1/ops/fleet"

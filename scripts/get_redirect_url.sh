#!/bin/bash


if [ -z "$1" ]; then
  echo "Usage: $0 <SHORT_CODE> [BASE_URL]"
  echo "Example: $0 8R6HyyFjcpa http://localhost:3000"
  exit 1
fi

SHORT_CODE=$1
BASE_URL=${2:-http://localhost:3000}

curl -i "${BASE_URL}/api/v1/${SHORT_CODE}"

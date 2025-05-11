#!/bin/bash
URL="$1"

if [ -z "$URL" ]; then
  echo "Usage: $0 <url>"
  exit 1
fi

echo "Checking health endpoint at $URL..."

for i in {1..5}; do
  response=$(curl --write-out '%{http_code}' --silent --output /dev/null "$URL")
  if [ "$response" -eq 200 ]; then
    echo "Health check passed."
    exit 0
  else
    echo "Health check failed with status $response. Retrying in 5 seconds..."
    sleep 5
  fi
done

echo "Health check failed after 5 attempts."
exit 1

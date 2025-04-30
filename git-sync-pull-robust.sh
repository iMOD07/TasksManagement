#!/bin/bash

echo "🔁 Checking for updates from GitHub..."
git fetch

LOCAL=$(git rev-parse @)
REMOTE=$(git rev-parse @{u})

if [ "$LOCAL" = "$REMOTE" ]; then
    echo "✅ Already up to date."
else
    echo "⬇️ Updates found. Pulling changes..."
    git pull
    echo "✅ Project updated successfully!"
fi

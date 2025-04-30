#!/bin/bash

# Ensure git uses merge strategy by default for pull operations
git config pull.rebase false

echo "🧠 Checking for changes..."
if git diff-index --quiet HEAD --; then
    echo "✅ No changes to commit. Working directory is clean."
    exit 0
fi

echo "📝 Staging all changes..."
git add .

echo "🗒️ Enter your commit message:"
read msg

git commit -m "$msg"
echo "🚀 Pushing to GitHub..."
git push
echo "✅ Code pushed successfully!"

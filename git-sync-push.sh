#!/bin/bash
echo "📝 Staging all changes..."
git add .

echo "🗒️ Enter your commit message:"
read msg

git commit -m "$msg"
echo "🚀 Pushing to GitHub..."
git push
echo "✅ Code pushed successfully!"

#!/bin/bash

echo "🚀 Complaint App Deployment Script"
echo "=================================="

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo "❌ Git is not installed. Please install git first."
    exit 1
fi

# Check if node is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js first."
    exit 1
fi

echo "✅ Prerequisites check passed"

# Initialize git if not already done
if [ ! -d ".git" ]; then
    echo "📁 Initializing git repository..."
    git init
    git add .
    git commit -m "Initial commit"
    echo "✅ Git repository initialized"
else
    echo "✅ Git repository already exists"
fi

echo ""
echo "🎯 Next Steps:"
echo "=============="
echo ""
echo "1. Create a GitHub repository:"
echo "   - Go to https://github.com/new"
echo "   - Name it: complaint-app"
echo "   - Make it public"
echo ""
echo "2. Push to GitHub:"
echo "   git remote add origin https://github.com/YOUR_USERNAME/complaint-app.git"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""
echo "3. Deploy to Render:"
echo "   - Go to https://render.com"
echo "   - Sign up/Login"
echo "   - Click 'New +' → 'Web Service'"
echo "   - Connect your GitHub repo"
echo "   - Configure:"
echo "     * Name: complaint-api-server"
echo "     * Root Directory: server"
echo "     * Build Command: npm install"
echo "     * Start Command: npm start"
echo ""
echo "4. Set Environment Variables in Render:"
echo "   - GMAIL_USER: your-email@gmail.com"
echo "   - GMAIL_PASS: your-gmail-app-password"
echo "   - NODE_ENV: production"
echo ""
echo "5. Update Android App:"
echo "   - Get your Render URL"
echo "   - Update BASE_URL in MainActivity.kt"
echo "   - Build production APK: ./gradlew assembleRelease"
echo ""
echo "📚 For detailed instructions, see DEPLOYMENT_GUIDE.md"
echo ""
echo "🎉 Your app will be ready for public use!" 
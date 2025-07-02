#!/bin/bash

echo "🚀 Setting up Complaint API Server with Real Email Functionality"
echo "================================================================"

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js first."
    echo "   Visit: https://nodejs.org/"
    exit 1
fi

echo "✅ Node.js found: $(node --version)"

# Check if npm is installed
if ! command -v npm &> /dev/null; then
    echo "❌ npm is not installed. Please install npm first."
    exit 1
fi

echo "✅ npm found: $(npm --version)"

# Navigate to server directory
cd server

echo ""
echo "📦 Installing server dependencies..."
npm install

if [ $? -eq 0 ]; then
    echo "✅ Dependencies installed successfully"
else
    echo "❌ Failed to install dependencies"
    exit 1
fi

echo ""
echo "🔧 Setting up environment variables..."

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    cp env.example .env
    echo "✅ Created .env file from template"
else
    echo "⚠️  .env file already exists"
fi

echo ""
echo "📧 EMAIL SETUP REQUIRED:"
echo "========================="
echo "1. Open server/.env file"
echo "2. Set your Gmail credentials:"
echo "   EMAIL_USER=rahul.s@atria.edu"
echo "   EMAIL_PASS=ubjg till vkom wumv"
echo ""
echo "3. For Gmail App Password:"
echo "   - Enable 2-Factor Authentication"
echo "   - Go to Google Account Settings"
echo "   - Security → 2-Step Verification → App passwords"
echo "   - Generate password for 'Mail'"
echo "   - Use that password in EMAIL_PASS"
echo ""

# Check if .env has been configured
if grep -q "ubjg till vkom wumv" .env; then
    echo "⚠️  WARNING: You need to configure email credentials in .env file"
    echo "   Edit server/.env and set your actual Gmail App Password"
else
    echo "✅ Email credentials appear to be configured"
fi

echo ""
echo "🌐 SERVER CONFIGURATION:"
echo "========================"
echo "Local server will run on: http://localhost:3000"
echo "Android app configured for: http://10.0.2.2:3000/api/ (emulator)"
echo "For real device, update BASE_URL in MainActivity.kt to your computer's IP"
echo ""

echo "🚀 To start the server:"
echo "======================="
echo "cd server"
echo "npm run dev"
echo ""
echo "📱 To test with Android app:"
echo "============================"
echo "1. Start the server (npm run dev)"
echo "2. Open the Android app"
echo "3. Submit a complaint"
echo "4. Check your email (amaresh.k@atria.edu) for the complaint"
echo ""

echo "✅ Setup complete! Follow the instructions above to start the server." 
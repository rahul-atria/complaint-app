# Gmail Setup Guide for Real Email Functionality

## 🚨 Current Issue
The server is working, but Gmail authentication is failing. You need to set up an App Password.

## 📧 Gmail App Password Setup

### Step 1: Enable 2-Factor Authentication
1. Go to [Google Account Settings](https://myaccount.google.com/)
2. Click on "Security" in the left sidebar
3. Under "Signing in to Google", click "2-Step Verification"
4. Enable 2-Step Verification if not already enabled

### Step 2: Generate App Password
1. Go back to [Google Account Settings](https://myaccount.google.com/)
2. Click on "Security"
3. Under "2-Step Verification", click "App passwords"
4. Select "Mail" from the dropdown
5. Click "Generate"
6. Copy the 16-character password (e.g., `abcd efgh ijkl mnop`)

### Step 3: Update Server Configuration
1. Open `server/.env` file
2. Replace the EMAIL_PASS line:
```
EMAIL_PASS=your-16-character-app-password
```
3. Replace `your-16-character-app-password` with the actual 16-character App Password from Step 2

### Step 4: Restart Server
```bash
cd server
# Stop current server (Ctrl+C if running)
node server.js
```

## ✅ Test Email Functionality

### Test Server Endpoint
```bash
curl -X POST http://localhost:3000/api/complaints \
  -H "Content-Type: application/json" \
  -d '{
    "category":"Maintenance",
    "location":"Room 101",
    "description":"AC not working",
    "submittedBy":"rahul.s@atria.edu",
    "id":"test-123",
    "timestamp":"2024-01-15 10:30:00"
  }'
```

### Test Android App
1. Open the Android app
2. Click on any category (Maintenance, Security, etc.)
3. Fill in location and description
4. Click "Submit Complaint"
5. Check amaresh.k@atria.edu inbox

## 🔧 Troubleshooting

### "Invalid login" Error
- Make sure you're using the App Password, not your regular Gmail password
- App Password is 16 characters with spaces
- Remove spaces when adding to .env file

### "Less secure app access" Error
- This is normal - you need to use App Passwords instead
- Follow the steps above to generate an App Password

### Server Connection Issues
- Make sure server is running: `node server.js`
- Check health endpoint: `curl http://localhost:3000/api/health`

## 📱 Android App Status
✅ Network security configured  
✅ Cleartext traffic allowed  
✅ Server communication working  
⏳ Email authentication pending (App Password setup)

## 🎯 Expected Result
After setting up the App Password correctly:
- Complaints from rahul.s@atria.edu will be sent to amaresh.k@atria.edu
- Beautiful HTML email templates with complaint details
- Real email delivery via Gmail SMTP 
# 🚀 Production Deployment Guide

## 📋 Overview
This guide will help you deploy your Complaint Registration App to the cloud so it can be used by the public from anywhere.

## 🎯 Quick Deployment Options

### Option 1: Render (Recommended - Free & Easy)

#### Step 1: Prepare Your Code
1. Create a GitHub repository
2. Push your code to GitHub:
```bash
git config --global user.email "rahul.s@atria.edu"
git config --global user.name "Rahul"

git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/rahul-atria/complaint-app.git
git push -u origin main
```

#### Step 2: Deploy to Render
1. Go to [render.com](https://render.com) and sign up
2. Click "New +" → "Web Service"
3. Connect your GitHub repository
4. Configure the service:
   - **Name**: complaint-api-server
   - **Root Directory**: server
   - **Build Command**: `npm install`
   - **Start Command**: `npm start`
   - **Environment**: Node

#### Step 3: Set Environment Variables
In Render dashboard, add these environment variables:
- `GMAIL_USER`: your-email@gmail.com
- `GMAIL_PASS`: your-gmail-app-password
- `NODE_ENV`: production

#### Step 4: Deploy
Click "Create Web Service" and wait for deployment.

### Option 2: Railway (Alternative)

#### Step 1: Install Railway CLI
```bash
npm install -g @railway/cli
```

#### Step 2: Deploy
```bash
cd server
railway login
railway init
railway up
```

#### Step 3: Set Environment Variables
```bash
railway variables set GMAIL_USER=your-email@gmail.com
railway variables set GMAIL_PASS=your-app-password
railway variables set NODE_ENV=production
```

## 📱 Update Android App

### Step 1: Get Your Server URL
After deployment, you'll get a URL like:
- Render: `https://your-app-name.onrender.com`
- Railway: `https://your-app-name.railway.app`

### Step 2: Update BASE_URL
Edit `app/src/main/java/com/example/atriapp/MainActivity.kt`:
```kotlin
private const val BASE_URL = "https://your-actual-url.com/api/"
```

### Step 3: Build Production APK
```bash
./gradlew assembleRelease
```

## 🔧 Gmail Setup for Production

### Step 1: Enable 2-Factor Authentication
1. Go to Google Account settings
2. Enable 2-Factor Authentication

### Step 2: Generate App Password
1. Go to Security settings
2. Click "App passwords"
3. Generate password for "Mail"
4. Use this password in your environment variables

## 📦 Distribution Options

### Option 1: Google Play Store (Recommended)
1. Create Google Play Console account
2. Upload your APK
3. Configure app details
4. Publish to store

### Option 2: Direct APK Distribution
1. Build release APK
2. Share APK file directly
3. Users enable "Install from unknown sources"

### Option 3: Internal Testing
1. Use Google Play Console internal testing
2. Invite testers via email
3. Test before public release

## 🔒 Security Considerations

### Server Security
- ✅ HTTPS enabled (automatic with cloud platforms)
- ✅ CORS configured
- ✅ Input validation
- ✅ Error handling
- ✅ Rate limiting (consider adding)

### App Security
- ✅ Network security config
- ✅ Input validation
- ✅ Error handling
- ✅ Secure storage for sensitive data

## 📊 Monitoring & Maintenance

### Health Checks
Your server includes health check endpoint:
```
GET https://your-server.com/api/health
```

### Logs
Monitor server logs in your cloud platform dashboard.

### Updates
- Keep dependencies updated
- Monitor for security patches
- Regular backups of environment variables

## 🎉 Final Steps

1. **Test thoroughly** with different devices and networks
2. **Monitor** server performance and logs
3. **Gather feedback** from initial users
4. **Iterate** based on feedback
5. **Scale** if needed (upgrade cloud plan)

## 📞 Support

### Common Issues
- **Email not sending**: Check Gmail app password
- **App can't connect**: Verify server URL and network
- **Server errors**: Check cloud platform logs

### Getting Help
- Check cloud platform documentation
- Review server logs
- Test endpoints manually with curl/Postman

## 🏆 Success Checklist

- [ ] Server deployed to cloud
- [ ] Environment variables configured
- [ ] Gmail app password set up
- [ ] Android app updated with server URL
- [ ] Production APK built
- [ ] App tested on multiple devices
- [ ] Distribution method chosen
- [ ] Monitoring set up
- [ ] Documentation complete

Your app is now ready for public use! 🎉 
# Cloud Deployment Options

## 🚀 Deploy to Cloud (No Same Network Required)

### Option 1: Railway (Free & Easy)
```bash
# 1. Install Railway CLI
npm install -g @railway/cli

# 2. Login to Railway
railway login

# 3. Initialize project
cd server
railway init

# 4. Deploy
railway up

# 5. Get your public URL
railway domain
```

### Option 2: Render (Free Tier)
```bash
# 1. Create account on render.com
# 2. Connect your GitHub repo
# 3. Deploy as Web Service
# 4. Get public URL automatically
```

### Option 3: Heroku (Free Tier Available)
```bash
# 1. Install Heroku CLI
curl https://cli-assets.heroku.com/install.sh | sh

# 2. Login
heroku login

# 3. Create app
heroku create your-complaint-app

# 4. Deploy
git add .
git commit -m "Deploy to Heroku"
git push heroku main

# 5. Get URL
heroku open
```

### Option 4: Vercel (Free)
```bash
# 1. Install Vercel CLI
npm i -g vercel

# 2. Deploy
cd server
vercel

# 3. Follow prompts
# 4. Get public URL
```

## 📱 Update Android App
Once deployed, update your `BASE_URL` in `MainActivity.kt`:
```kotlin
private const val BASE_URL = "https://your-app-name.railway.app/api/"
// or
private const val BASE_URL = "https://your-app-name.onrender.com/api/"
// or
private const val BASE_URL = "https://your-app-name.herokuapp.com/api/"
```

## 🔑 Environment Variables
Set these in your cloud platform:
- `GMAIL_USER`: your-email@gmail.com
- `GMAIL_PASS`: your-app-password

## ✅ Benefits
- ✅ Works from anywhere
- ✅ No network restrictions
- ✅ Always accessible
- ✅ Professional deployment
- ✅ HTTPS by default 
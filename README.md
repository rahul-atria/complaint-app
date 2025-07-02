# 🏛️ Atria Engineering College - Complaint Registration App

A modern, production-ready complaint registration system for Atria Engineering College, built with **Android Jetpack Compose** and **Node.js**.

## 🌟 Features

### 📱 Android App
- **Modern UI**: Beautiful Jetpack Compose interface
- **Camera Integration**: Take photos directly in the app
- **Gallery Support**: Attach existing images
- **Real-time Validation**: Form validation and error handling
- **Network Security**: Secure HTTPS connections
- **Responsive Design**: Works on all Android devices

### 🖥️ Backend Server
- **Email Automation**: Automatic complaint routing via Gmail
- **Category-based Routing**: Smart email distribution
- **Image Attachments**: Support for photo attachments
- **Production Ready**: Cloud deployment ready
- **Security**: CORS, input validation, error handling

## 🚀 Quick Start

### Prerequisites
- Android Studio
- Node.js (v18+)
- Gmail account with App Password

### 1. Clone & Setup
```bash
git clone https://github.com/yourusername/complaint-app.git
cd complaint-app
```

### 2. Server Setup
```bash
cd server
npm install
cp .env.example .env
# Edit .env with your Gmail credentials
npm start
```

### 3. Android App
```bash
# Open in Android Studio
# Build and run on device/emulator
```

## 📦 Production Deployment

### Quick Deploy
```bash
./deploy.sh
```

### Manual Deployment
1. **Deploy Server**: Follow `DEPLOYMENT_GUIDE.md`
2. **Update App**: Change `BASE_URL` in `MainActivity.kt`
3. **Build APK**: `./gradlew assembleRelease`
4. **Distribute**: Google Play Store or direct APK

## 🏗️ Architecture

```
┌─────────────────┐    HTTPS    ┌─────────────────┐    SMTP    ┌─────────────┐
│   Android App   │ ──────────► │  Node.js Server │ ─────────► │   Gmail     │
│  (Jetpack       │             │  (Express.js)   │            │             │
│   Compose)      │             │                 │            │             │
└─────────────────┘             └─────────────────┘            └─────────────┘
```

## 📋 Complaint Categories

| Category | Recipient Email | Description |
|----------|----------------|-------------|
| Infrastructure | infrastructure@atria.edu | Building, electrical, plumbing issues |
| Academic | academic@atria.edu | Course, faculty, curriculum concerns |
| Hostel | hostel@atria.edu | Accommodation, facilities issues |
| Transportation | transport@atria.edu | Bus, parking, transport problems |
| Food | food@atria.edu | Canteen, food quality issues |
| Other | admin@atria.edu | General complaints |

## 🔧 Configuration

### Environment Variables
```bash
GMAIL_USER=your-email@gmail.com
GMAIL_PASS=your-app-password
NODE_ENV=production
PORT=3000
```

### Android Configuration
```kotlin
// Update in MainActivity.kt
private const val BASE_URL = "https://your-server.com/api/"
```

## 📱 Screenshots

### Home Screen
- Category selection cards
- Modern gradient design
- Smooth animations

### Complaint Form
- Location picker
- Description field
- Image attachment
- Real-time validation

### Success Screen
- Confirmation message
- Email details
- Return to home

## 🔒 Security Features

### Server Security
- ✅ HTTPS encryption
- ✅ CORS protection
- ✅ Input validation
- ✅ Rate limiting ready
- ✅ Error handling

### App Security
- ✅ Network security config
- ✅ Input sanitization
- ✅ Secure storage
- ✅ Permission handling

## 📊 API Endpoints

### Health Check
```
GET /api/health
Response: Server status and configuration
```

### Submit Complaint
```
POST /api/complaints
Body: {
  "category": "Infrastructure",
  "location": "Block A, Room 101",
  "description": "Broken window",
  "imageBase64": "data:image/jpeg;base64,...",
  "senderEmail": "student@atria.edu"
}
```

## 🛠️ Development

### Server Development
```bash
cd server
npm run dev  # Auto-restart on changes
```

### Android Development
- Open in Android Studio
- Use emulator or real device
- Enable developer options for debugging

### Testing
```bash
# Test server
curl http://localhost:3000/api/health

# Test complaint submission
curl -X POST http://localhost:3000/api/complaints \
  -H "Content-Type: application/json" \
  -d '{"category":"Test","location":"Test","description":"Test","senderEmail":"test@test.com"}'
```

## 📈 Monitoring

### Health Monitoring
- Server health endpoint
- Email delivery status
- Error logging

### Performance
- Response time monitoring
- Error rate tracking
- User feedback collection

## 🤝 Contributing

1. Fork the repository
2. Create feature branch
3. Make changes
4. Test thoroughly
5. Submit pull request

## 📄 License

MIT License - see LICENSE file for details

## 🆘 Support

### Common Issues
- **Email not sending**: Check Gmail app password
- **App connection**: Verify server URL
- **Build errors**: Check Android Studio logs

### Getting Help
- Check documentation
- Review error logs
- Test endpoints manually

## 🎉 Acknowledgments

- Atria Engineering College
- Jetpack Compose team
- Node.js community
- Gmail API

---

**Built with ❤️ for Atria Engineering College**

*Making complaint management simple and efficient* 
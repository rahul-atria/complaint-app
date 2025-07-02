# Network Connectivity Troubleshooting

## 🚨 Current Issue
Android app can't connect to server: `failed to connect to /10.0.10.137(port 3000)`

## 🔍 Possible Causes & Solutions

### 1. **Different Networks**
**Problem**: Android device and computer are on different WiFi networks
**Solution**: 
- Connect both devices to the same WiFi network
- Check if your computer and phone are on the same network

### 2. **Firewall Blocking**
**Problem**: Computer firewall blocking port 3000
**Solution**:
```bash
# Allow port 3000 through firewall
sudo ufw allow 3000
```

### 3. **Router/Network Restrictions**
**Problem**: Router blocking device-to-device communication
**Solution**:
- Check router settings
- Try using mobile hotspot from phone

### 4. **IP Address Changed**
**Problem**: Computer IP address changed
**Solution**:
```bash
# Check current IP
hostname -I
# Update BASE_URL in MainActivity.kt with new IP
```

## 🧪 Quick Tests

### Test 1: Ping Test
On your Android device, try to ping the computer:
```bash
# From terminal or use a network app
ping 10.0.10.137
```

### Test 2: Browser Test
On your Android device, open browser and go to:
```
http://10.0.10.137:3000/api/health
```
Should show: `{"success":true,"message":"Complaint API Server is running"}`

### Test 3: Server Status
```bash
# Check if server is running
ps aux | grep "node server.js"

# Check if listening on all interfaces
ss -tlnp | grep :3000
# Should show: *:3000
```

## 🔧 Alternative Solutions

### Solution 1: Use Emulator
```bash
# Change BASE_URL back to emulator
private const val BASE_URL = "http://10.0.2.2:3000/api/"
```

### Solution 2: Use Local Network IP
```bash
# Find your local network IP
ip route get 1.1.1.1 | awk '{print $7}'
# Update BASE_URL with this IP
```

### Solution 3: Use ngrok (Temporary)
```bash
# Install ngrok
npm install -g ngrok

# Create tunnel
ngrok http 3000

# Use the ngrok URL in BASE_URL
# Example: https://abc123.ngrok.io/api/
```

## 📱 Current Configuration
- **Server IP**: 10.0.10.137
- **Port**: 3000
- **URL**: http://10.0.10.137:3000/api/
- **Status**: Server running, emails working

## 🎯 Next Steps
1. Try the browser test on your phone
2. Check if both devices are on same network
3. Try the alternative solutions above 
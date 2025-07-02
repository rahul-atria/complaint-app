# Complaint API Server

A Node.js backend server for the Complaint Registration App with real email functionality.

## Features

- ✅ Real email sending using Nodemailer
- ✅ Special routing: rahul.s@atria.edu → amaresh.k@atria.edu
- ✅ Category-based routing for other users
- ✅ Beautiful HTML email templates
- ✅ RESTful API endpoints
- ✅ CORS enabled for mobile app

## Setup Instructions

### 1. Install Dependencies
```bash
cd server
npm install
```

### 2. Configure Email Settings
Create a `.env` file in the server directory:
```bash
cp env.example .env
```

Edit `.env` with your email credentials:
```
EMAIL_USER=rahul.s@atria.edu
EMAIL_PASS=your-app-password
PORT=3000
```

### 3. Gmail Setup (Required)
1. Enable 2-Factor Authentication on your Gmail account
2. Generate an App Password:
   - Go to Google Account Settings
   - Security → 2-Step Verification → App passwords
   - Generate password for "Mail"
3. Use the generated password in `.env`

### 4. Start Server
```bash
# Development mode
npm run dev

# Production mode
npm start
```

## API Endpoints

### POST /api/complaints
Submit a new complaint and send email.

**Request Body:**
```json
{
  "id": "uuid",
  "category": "Maintenance",
  "location": "Room 101",
  "description": "AC not working",
  "submittedBy": "rahul.s@atria.edu",
  "timestamp": "2024-01-15 10:30:00"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Complaint submitted successfully! Email sent to amaresh.k@atria.edu",
  "complaintId": "uuid",
  "emailId": "email-message-id"
}
```

### GET /api/health
Health check endpoint.

## Email Routing

- **rahul.s@atria.edu** → **amaresh.k@atria.edu** (all categories)
- **Other users** → Category-based routing:
  - Maintenance → maintenance@college.edu
  - Security → security@college.edu
  - Building → facilities@college.edu
  - Store → store@college.edu
  - Housekeeping → housekeeping@college.edu

## Deployment

### Local Development
```bash
npm run dev
```

### Production (Heroku)
1. Create Heroku app
2. Set environment variables
3. Deploy: `git push heroku main`

### Production (Other Platforms)
- Set `PORT` environment variable
- Configure email credentials
- Deploy Node.js app

## Troubleshooting

### Email Not Sending
1. Check Gmail App Password is correct
2. Verify 2-Factor Authentication is enabled
3. Check server logs for error messages

### CORS Issues
- Server has CORS enabled for all origins
- If issues persist, check client URL configuration

### Port Issues
- Default port: 3000
- Can be changed via `PORT` environment variable 
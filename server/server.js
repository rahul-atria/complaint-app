const express = require('express');
const nodemailer = require('nodemailer');
const cors = require('cors');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3000;

// Enhanced CORS configuration for production
app.use(cors({
    origin: ['http://localhost:3000', 'https://your-app-domain.com', '*'],
    credentials: true,
    methods: ['GET', 'POST', 'OPTIONS'],
    allowedHeaders: ['Content-Type', 'Authorization']
}));

// Security headers
app.use((req, res, next) => {
    res.header('X-Content-Type-Options', 'nosniff');
    res.header('X-Frame-Options', 'DENY');
    res.header('X-XSS-Protection', '1; mode=block');
    next();
});

app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true, limit: '10mb' }));

// Email configuration with better error handling
const createTransporter = () => {
    try {
        return nodemailer.createTransport({
            service: 'gmail',
            auth: {
                user: process.env.GMAIL_USER || 'rahul.s@atria.edu',
                pass: process.env.GMAIL_PASS || 'your-app-password'
            },
            tls: {
                rejectUnauthorized: false
            }
        });
    } catch (error) {
        console.error('Failed to create email transporter:', error);
        return null;
    }
};

const transporter = createTransporter();

// Health check endpoint
app.get('/api/health', (req, res) => {
    res.json({
        success: true,
        message: 'Complaint API Server is running',
        timestamp: new Date().toISOString(),
        environment: process.env.NODE_ENV || 'development',
        emailConfigured: !!transporter
    });
});

// Complaint submission endpoint with enhanced error handling
app.post('/api/complaints', async (req, res) => {
    try {
        const { category, location, description, imageBase64, senderEmail } = req.body;

        // Input validation
        if (!category || !location || !description || !senderEmail) {
            return res.status(400).json({
                success: false,
                message: 'Missing required fields: category, location, description, senderEmail'
            });
        }

        // Email validation
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(senderEmail)) {
            return res.status(400).json({
                success: false,
                message: 'Invalid email format'
            });
        }

        // Determine recipient based on category and sender
        let recipientEmail = 'admin@atria.edu'; // Default recipient
        
        const categoryRecipients = {
            'Infrastructure': 'infrastructure@atria.edu',
            'Academic': 'academic@atria.edu',
            'Hostel': 'hostel@atria.edu',
            'Transportation': 'transport@atria.edu',
            'Food': 'food@atria.edu',
            'Other': 'admin@atria.edu'
        };

        if (categoryRecipients[category]) {
            recipientEmail = categoryRecipients[category];
        }

        // Special routing for rahul.s@atria.edu
        if (senderEmail === 'rahul.s@atria.edu') {
            recipientEmail = 'amaresh.k@atria.edu';
        }

        // Create email content
        const emailContent = `
            <h2>New Complaint Registration</h2>
            <p><strong>Category:</strong> ${category}</p>
            <p><strong>Location:</strong> ${location}</p>
            <p><strong>Description:</strong> ${description}</p>
            <p><strong>From:</strong> ${senderEmail}</p>
            <p><strong>Date:</strong> ${new Date().toLocaleString()}</p>
            ${imageBase64 ? '<p><strong>Image:</strong> Attached</p>' : ''}
        `;

        // Send email
        if (!transporter) {
            throw new Error('Email service not configured');
        }

        const mailOptions = {
            from: process.env.GMAIL_USER || 'rahul.s@atria.edu',
            to: recipientEmail,
            subject: `[COMPLAINT] ${category} - ${location}`,
            html: emailContent,
            attachments: imageBase64 ? [{
                filename: 'complaint_image.jpg',
                content: imageBase64,
                encoding: 'base64'
            }] : []
        };

        const info = await transporter.sendMail(mailOptions);
        console.log('Email sent successfully:', info.messageId);
        console.log(`From: ${senderEmail} To: ${recipientEmail}`);

        res.json({
            success: true,
            message: 'Complaint submitted successfully',
            complaintId: info.messageId,
            recipient: recipientEmail,
            timestamp: new Date().toISOString()
        });

    } catch (error) {
        console.error('Error processing complaint:', error);
        res.status(500).json({
            success: false,
            message: 'Failed to submit complaint. Please try again.',
            error: process.env.NODE_ENV === 'development' ? error.message : 'Internal server error'
        });
    }
});

// Error handling middleware
app.use((error, req, res, next) => {
    console.error('Unhandled error:', error);
    res.status(500).json({
        success: false,
        message: 'Internal server error',
        error: process.env.NODE_ENV === 'development' ? error.message : 'Something went wrong'
    });
});

// 404 handler
app.use('*', (req, res) => {
    res.status(404).json({
        success: false,
        message: 'Endpoint not found'
    });
});

// Start server
app.listen(PORT, '0.0.0.0', () => {
    console.log(`🚀 Complaint API Server running on port ${PORT}`);
    console.log(`📧 Email service configured for: ${process.env.GMAIL_USER || 'rahul.s@atria.edu'}`);
    console.log(`🌐 Health check: http://localhost:${PORT}/api/health`);
    console.log(`🌍 Environment: ${process.env.NODE_ENV || 'development'}`);
}); 
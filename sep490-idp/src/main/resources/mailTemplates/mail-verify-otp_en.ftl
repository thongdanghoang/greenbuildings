<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify Your Email</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap');

        .container {
            max-width: 600px;
            margin: 0 auto;
            font-family: 'Inter', Arial, sans-serif;
            line-height: 1.6;
            color: #333333;
        }

        .header {
            background-color: #0056b3;
            padding: 25px;
            text-align: center;
            border-radius: 8px 8px 0 0;
        }

        .content {
            padding: 30px;
            background-color: #ffffff;
            border: 1px solid #e0e0e0;
            border-top: none;
            border-radius: 0 0 8px 8px;
        }

        .otp-code {
            font-size: 32px;
            font-weight: 700;
            letter-spacing: 3px;
            color: #0056b3;
            text-align: center;
            margin: 25px 0;
            padding: 15px 0;
            background-color: #f5f9ff;
            border-radius: 6px;
        }

        .footer {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eeeeee;
            font-size: 14px;
            color: #777777;
        }

        .button {
            display: inline-block;
            padding: 12px 24px;
            background-color: #0056b3;
            color: white !important;
            text-decoration: none;
            border-radius: 6px;
            font-weight: 500;
            margin: 15px 0;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1 style="color: white; margin: 0; font-size: 24px;">Verify Your Email Address</h1>
    </div>

    <div class="content">
        <p>Hello <strong>${userEmail}</strong>,</p>

        <p>Thank you for registering with us. To complete your registration, please verify your email address by entering the
            following One-Time Password (OTP):</p>

        <div class="otp-code">${otpCode}</div>

        <p>This verification code will expire in <strong>10 minutes</strong>. If you didn't request this, please ignore this email
            or contact support if you have questions.</p>

        <div class="footer">
            <p>Need help? <a href="mailto:support@greenbuilding.com" style="color: #0056b3; text-decoration: none;">Contact our
                    support team</a></p>
            <p>© 2023 Your Company Name. All rights reserved.</p>
        </div>
    </div>
</div>
</body>
</html>
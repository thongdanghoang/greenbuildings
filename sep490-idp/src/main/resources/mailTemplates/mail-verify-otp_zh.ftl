<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>验证您的邮箱</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@400;500;700&display=swap');

        .container {
            max-width: 600px;
            margin: 0 auto;
            font-family: 'Noto Sans SC', 'Inter', Arial, sans-serif;
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
        <h1 style="color: white; margin: 0; font-size: 24px;">验证您的邮箱地址</h1>
    </div>

    <div class="content">
        <p>尊敬的 <strong>${userEmail}</strong> 用户，</p>

        <p>感谢您的注册。请使用以下一次性验证码（OTP）完成邮箱验证：</p>

        <div class="otp-code">${otpCode}</div>

        <p>此验证码将在 <strong>10分钟</strong> 后失效。如非本人操作，请忽略此邮件或联系客服。</p>

        <div class="footer">
            <p>需要帮助？ <a href="mailto:support@greenbuilding.com" style="color: #0056b3; text-decoration: none;">联系客服团队</a>
            </p>
            <p>© 2023 您的公司名称。保留所有权利。</p>
        </div>
    </div>
</div>
</body>
</html>
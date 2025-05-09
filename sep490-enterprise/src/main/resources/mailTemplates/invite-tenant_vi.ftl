<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${subject}</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
            line-height: 1.6;
            color: #333;
        }
        .container {
            max-width: 600px;
            margin: 40px auto;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        .header {
            background-color: #0073e6;
            padding: 20px;
            text-align: center;
            color: #ffffff;
        }
        .header h1 {
            margin: 0;
            font-size: 24px;
        }
        .content {
            padding: 30px;
        }
        .content p {
            margin: 0 0 20px;
        }
        .button {
            display: inline-block;
            padding: 12px 24px;
            background-color: #0073e6;
            color: #ffffff;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
            transition: background-color 0.3s;
        }
        .button:hover {
            background-color: #005bb5;
        }
        .footer {
            background-color: #f4f4f9;
            padding: 20px;
            text-align: center;
            font-size: 14px;
            color: #666;
        }
        .footer a {
            color: #0073e6;
            text-decoration: none;
        }
        @media only screen and (max-width: 600px) {
            .container {
                margin: 20px;
            }
            .header h1 {
                font-size: 20px;
            }
            .content {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Chào mừng bạn đến với ${appName}</h1>
    </div>
    <div class="content">
        <p>Xin chào ${tenantName},</p>
        <p>Tài khoản của bạn đã được <a href="mailto:${ownerEmail}">${ownerEmail}</a> mời tham gia nhóm tòa nhà <strong>${buildingGroupName}</strong> của tòa nhà <strong>${buildingName}</strong>.</p>
        <p>Để chấp nhận lời mời và trở thành một phần của cộng đồng chúng tôi, vui lòng nhấp vào nút dưới đây để truy cập ${appName} và xác nhận tham gia.</p>
        <p style="text-align: center;">
            <a href="${appUrl}" class="button">Đến trang web</a>
        </p>
    </div>
    <div class="footer">
        <p>Trân trọng,<br><strong>Đội ngũ ${appName}</strong></p>
        <p>Cần hỗ trợ? Liên hệ với chúng tôi tại <a href="mailto:support@${appName}.com">support@${appName}.com</a></p>
    </div>
</div>
</body>
</html>
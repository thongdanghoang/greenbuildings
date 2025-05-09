<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${subject}</title>
    <style>
        body {
            font-family: 'Arial', 'Microsoft YaHei', sans-serif;
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
        <h1>欢迎体验 ${appName}</h1>
    </div>
    <div class="content">
        <p>尊敬的 ${tenantName},</p>
        <p>您的帐户已被 <a href="mailto:${ownerEmail}">${ownerEmail}</a> 邀请加入建筑物 <strong>${buildingName}</strong> 的建筑组 <strong>${buildingGroupName}</strong>。</p>
        <p>如您同意加入建筑组 <strong>${buildingGroupName}</strong>，请点击下方按钮访问 ${appName} 以接受邀请，加入我们的社区。</p>
        <p style="text-align: center;">
            <a href="${appUrl}" class="button">前往网站</a>
        </p>
    </div>
    <div class="footer">
        <p>此致,<br><strong>${appName} 团队</strong></p>
        <p>需要帮助？请联系我们：<a href="mailto:support@${appName}.com">support@${appName}.com</a></p>
    </div>
</div>
</body>
</html>
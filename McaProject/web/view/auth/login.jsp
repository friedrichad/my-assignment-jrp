<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <!-- Liên kết CSS đúng với context path -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
        <style>
            body {
                font-family: sans-serif;
                background: #f6f6f6;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
            }
            .login-box {
                width: 320px;
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                border: 1px solid #1d7484; /* màu xanh chủ đạo của Sakura */
                border-radius: 4px;
                padding: 20px;
                background-color: #fff;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .block.input {
                width: 100%;
                margin: 6px 0 12px;
                padding: 10px;
                font-weight: normal;
            }
            .block.title {
                text-align: center;
                font-size: 1.5em;
                margin-bottom: 20px;
            }
            .error {
                text-align: center;
                margin-top: 10px;
                color: red;
            }
        </style>
    </head>
    <body>
        <div class="login-box">
            <div class="block title accent round">🔐 Login</div>
            <form action="${pageContext.request.contextPath}/login" method="post" class="block">
                <label class="block">Username:</label>
                <input type="text" name="account" required class="block input">

                <label class="block">Password:</label>
                <input type="password" name="password" required class="block input">

                <button type="submit" class="block accent round">Login</button>

                <div class="error block round">
                    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
                </div>
            </form>
        </div>
    </body>
</html>

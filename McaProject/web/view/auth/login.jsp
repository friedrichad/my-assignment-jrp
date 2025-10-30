<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <!-- Link Sakura CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
        <script src="${pageContext.request.contextPath}/assets/js/toggle-password.js"></script>

        <style>
            body {
                background-color: #f0f2f5; /* giữ nền xám nhẹ */
                display: flex;
                justify-content: center;  /* căn giữa theo chiều ngang */
                align-items: center;      /* căn giữa theo chiều dọc */
                min-height: 100vh;        /* chiếm đủ chiều cao màn hình */
                margin: 0;                /* bỏ margin mặc định */
                max-width: none !important; /* ghi đè sakura.css */
            }

            .container {
                display: flex;
                justify-content: center;
                align-items: center;
                background: white;
                border-radius: 12px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                width: 900px;
                max-width: 95%;
            }

            .image-box {
                flex: 1;
                background: #fafafa;
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 20px;
            }

            .image-box img {
                width: 100%;
                max-width: 400px;
                height: auto;
                border-radius: 8px;
            }

            .login-box {
                flex: 1;
                padding: 40px;
            }

            .login-box h1 {
                text-align: center;
                margin-bottom: 24px;
            }

            .login-box input[type="text"],
            .login-box input[type="password"] {
                width: 100%;
                padding: 14px 18px;
                font-size: 1.1rem;
                margin-bottom: 16px;
                box-sizing: border-box;
            }

            .login-box button {
                width: 100%;
                padding: 14px;
                font-size: 1.2rem;
            }

            .error {
                text-align: center;
                margin-top: 12px;
                color: red;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <div>
            <div class="container">
                <div class="image-box">
                    <img src="${pageContext.request.contextPath}/assets/images/login-side.png" alt="Login illustration">
                </div>

                <div class="login-box">
                    <h1>🔐 Login</h1>
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <label>Username:</label>
                        <input type="text" name="account" required>
                        <div style="position: relative;">
                            <input type="password" name="password" id="password" required>
                            <span onclick="togglePassword()" style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); cursor: pointer;">
                                👁️
                            </span>
                        </div>

                        <button type="submit">Login</button>


                        <%
                            String err = (String) request.getAttribute("error");
                            if (err != null && !err.isEmpty()) {
                        %>
                        <div class="error"><%= err %></div>
                        <%
                            }
                        %>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>

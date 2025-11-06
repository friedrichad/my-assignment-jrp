<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <!-- Link Sakura CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">

    <!-- Toggle password JS -->
    <script>
        function togglePassword() {
            const pw = document.getElementById("password");
            if (pw.type === "password") {
                pw.type = "text";
            } else {
                pw.type = "password";
            }
        }
    </script>

    <style>
        /* Ghi đè body Sakura */
        body {
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            max-width: none !important; /* override Sakura max-width */
            padding: 0 10px;
        }

        .container {
            display: flex;
            flex-wrap: wrap; /* responsive */
            justify-content: center;
            align-items: stretch;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            overflow: hidden;
            width: 900px;
            max-width: 100%;
        }

        .image-box {
            flex: 1;
            background: #fafafa;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
            min-width: 250px;
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
            min-width: 250px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .login-box h1 {
            text-align: center;
            margin-bottom: 24px;
        }

        .login-box label {
            display: block;
            margin-bottom: 4px;
            font-weight: 600;
        }

        .login-box input[type="text"],
        .login-box input[type="password"] {
            width: 100%;
            padding: 14px 18px;
            font-size: 1.1rem;
            margin-bottom: 16px;
            box-sizing: border-box;
            border-radius: 4px;
        }

        .login-box button {
            width: 100%;
            padding: 14px;
            font-size: 1.2rem;
            background-color: #1d7484;
            color: #fff;
            border: none;
            cursor: pointer;
            border-radius: 4px;
        }

        .login-box button:hover {
            background-color: #982c61;
        }

        .error {
            text-align: center;
            margin-top: 12px;
            color: red;
            font-weight: bold;
        }

        /* Toggle password eye */
        .password-wrapper {
            position: relative;
        }
        .password-wrapper span {
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
            font-size: 1.2rem;
        }

        /* Responsive small screens */
        @media (max-width: 768px) {
            .container {
                flex-direction: column;
            }
            .image-box, .login-box {
                min-width: 100%;
            }
            .login-box {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="image-box">
            <img src="${pageContext.request.contextPath}/assets/images/login-side.png" alt="Login illustration">
        </div>

        <div class="login-box">
            <h1>Login</h1>
            <form action="${pageContext.request.contextPath}/login" method="post">
                <label for="account">Username:</label>
                <input type="text" id="account" name="account" required>

                <label for="password">Password:</label>
                <div class="password-wrapper">
                    <input type="password" id="password" name="password" required>
                    <span onclick="togglePassword()">👁️</span>
                </div>

                <button type="submit">Login</button>

                <% String err = (String) request.getAttribute("error");
                   if (err != null && !err.isEmpty()) { %>
                <div class="error"><%= err %></div>
                <% } %>
            </form>
        </div>
    </div>
</body>
</html>
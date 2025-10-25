<%-- 
    Document   : login
    Created on : Oct 25, 2025, 11:26:06 AM
    Author     : Hiro
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <style>
        body { font-family: sans-serif; background: #f6f6f6; }
        .container {
            width: 300px; margin: 100px auto; background: #fff; padding: 20px;
            border-radius: 10px; box-shadow: 0 0 5px rgba(0,0,0,0.1);
        }
        input { width: 100%; margin: 8px 0; padding: 8px; }
        button { width: 100%; padding: 10px; background: #007bff; color: white; border: none; border-radius: 5px; }
        .error { color: red; text-align: center; }
    </style>
</head>
<body>
<div class="container">
    <h2>Login</h2>
    <form action="login" method="post">
        <label>Username:</label>
        <input type="text" name="account" required>

        <label>Password:</label>
        <input type="password" name="password" required>

        <button type="submit">Login</button>

        <div class="error">
            <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
        </div>
    </form>
</div>
</body>
</html>

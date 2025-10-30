<%-- 
    Document   : header
    Created on : Oct 25, 2025, 10:34:25 PM
    Author     : Hiro
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>MyAssignmentPrj</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
    <style>
        /* === HEADER CHUNG === */
        header {
            background-color: #ffffff;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 10px 0;
            border-radius: 8px;
        }
        

        /* === LOGO === */
        .logo {
            font-size: 28px;
            font-weight: bold;
            color: #4a90e2;
            text-decoration: none;
            margin-bottom: 10px;
        }

        /* === NAVBAR === */
        nav ul {
            list-style: none;
            display: flex;
            gap: 24px;
            padding: 0;
            margin: 0;
        }

        nav ul li a {
            text-decoration: none;
            color: #333;
            font-weight: 500;
            transition: 0.2s;
        }

        nav ul li a:hover {
            color: #4a90e2;
        }

        /* === ROLE INFO === */
        .user-info {
            margin-top: 10px;
            font-size: 14px;
            color: #555;
        }
    </style>
</head>
<body>
<header>
    <!-- Logo -->
    <a href="${pageContext.request.contextPath}/index.jsp" class="logo">MyAssignmentPrj</a>

    <!-- Navbar -->
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a></li>
            <li><a href="${pageContext.request.contextPath}/news">Tin tức</a></li>
            <li><a href="${pageContext.request.contextPath}/services">Dịch vụ</a></li>
            <li><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
        </ul>
    </nav>

    <!-- Role hiển thị -->
    <div class="user-info">
        <%
            String role = (String) session.getAttribute("role");
            if (role == null) {
                role = "Guest";
            }
        %>
        Vai trò: <strong><%= role %></strong>
    </div>
</header>

<!-- Nội dung của trang -->
<div class="content" style="padding:20px;">

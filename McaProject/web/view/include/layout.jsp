<%-- 
    Document   : layout.jsp
    Created on : Nov 6, 2025
    Author     : Hiro
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${pageTitle != null ? pageTitle : 'Quản lý nhân viên'}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
    <script defer src="${pageContext.request.contextPath}/assets/js/alpine.min.js"></script>

    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: #f6f7fb;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #374151;
            color: white;
            padding: 15px 25px;
            font-size: 1.2em;
        }

        .container {
            display: flex;
            min-height: 90vh;
        }

        aside {
            width: 220px;
            background: #1f2937;
            color: #f3f4f6;
            padding: 20px 0;
            display: flex;
            flex-direction: column;
        }

        aside h2 {
            text-align: center;
            font-size: 1.1em;
            margin-bottom: 10px;
        }

        aside a {
            color: #d1d5db;
            text-decoration: none;
            padding: 10px 20px;
            display: block;
            border-left: 4px solid transparent;
            transition: 0.3s;
        }

        aside a:hover, aside a.active {
            background: #374151;
            color: #fff;
            border-left-color: #3b82f6;
        }

        main {
            flex: 1;
            padding: 25px;
            background: #fff;
            margin: 15px;
            border-radius: 10px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.1);
        }

        footer {
            text-align: center;
            padding: 10px;
            background-color: #e5e7eb;
            color: #6b7280;
        }
    </style>
</head>

<body>
<header>
    🌸 <strong>Hệ thống quản lý nhân viên</strong>
</header>

<div class="container">
    <!-- SIDEBAR -->
    <aside>
        <h2>Menu</h2>
        <a href="${pageContext.request.contextPath}/dashboard" 
           class="${pageTitle == 'Dashboard' ? 'active' : ''}">🏠 Dashboard</a>
        <a href="${pageContext.request.contextPath}/employee/list" 
           class="${pageTitle == 'Nhân viên' ? 'active' : ''}">👥 Nhân viên</a>
        <a href="${pageContext.request.contextPath}/request/list" 
           class="${pageTitle == 'Đơn nghỉ phép' ? 'active' : ''}">📝 Đơn nghỉ phép</a>
        <a href="${pageContext.request.contextPath}/logout">🚪 Đăng xuất</a>
    </aside>

    <!-- MAIN CONTENT -->
    <main>
        <jsp:include page="${contentPage}" />
    </main>
</div>

<footer>
    © 2025 - Quản lý nhân viên | Thiết kế bởi Hiro 🌸
</footer>
</body>
</html>

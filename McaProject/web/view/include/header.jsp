<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${pageTitle != null ? pageTitle : "Hệ thống quản lý nghỉ phép"}</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
<script defer src="${pageContext.request.contextPath}/assets/js/alpine.min.js"></script>

<style>
    header {
        background-color: #f6f6f6;
        padding: 10px 20px;
        border-bottom: 1px solid #ddd;
    }
    header h1 {
        font-size: 20px;
        margin: 0;
    }
    nav a {
        margin-right: 15px;
        text-decoration: none;
        color: #007bff;
    }
    nav a:hover {
        text-decoration: underline;
    }
</style>

<header>
    <h1>🌸 Hệ thống nghỉ phép</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/request/list">Danh sách đơn</a>
        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </nav>
</header>
<%-- 
    Document   : header
    Created on : Oct 25, 2025, 10:34:25 PM
    Author     : Hiro
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>MyAssignmentPrj</title>
    <!-- Link Sakura CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
</head>
<body>
    <header>
        <nav>
            <ul style="list-style:none; padding:0; margin:0; display:flex;">
                <li style="margin-right:16px;"><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
                <li style="margin-right:16px;"><a href="${pageContext.request.contextPath}/leave-request">Xin nghỉ phép</a></li>
                <li style="margin-right:16px;"><a href="${pageContext.request.contextPath}/manage-leave">Quản lý đơn</a></li>
                <li style="margin-right:16px;"><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
            </ul>
        </nav>
        <div style="padding:8px 16px;">
            <!-- Hiển thị role người dùng -->
            <%
                String role = (String) session.getAttribute("role");
                if (role == null) {
                    role = "Guest";
                }
            %>
            <p>Role: <strong><%= role %></strong></p>
        </div>
    </header>

    <div class="content" style="padding:20px;">
        <!-- Nội dung trang -->

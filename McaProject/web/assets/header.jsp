<%-- 
    Document   : header
    Created on : Oct 25, 2025, 10:34:25 PM
    Author     : Hiro
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MyAssignmentPrj</title>
    <style>
        body { font-family: Arial, sans-serif; margin:0; padding:0;}
        .navbar { background-color: #333; overflow: hidden; }
        .navbar a { float: left; display: block; color: white; text-align: center;
                    padding: 14px 16px; text-decoration: none; }
        .navbar a:hover { background-color: #ddd; color: black; }
        .content { padding: 20px; }
    </style>
</head>
<body>
<div class="navbar">
    <a href="<%=request.getContextPath()%>/index.jsp">Home</a>
    <a href="<%=request.getContextPath()%>/leave-request">Xin nghỉ phép</a>
    <a href="<%=request.getContextPath()%>/manage-leave">Quản lý đơn</a>
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
</div>
<div class="content">

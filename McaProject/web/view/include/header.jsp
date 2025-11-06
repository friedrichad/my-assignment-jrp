<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi" x-data>
<head>
    <meta charset="UTF-8">
    <title><%= request.getAttribute("pageTitle") != null ? request.getAttribute("pageTitle") : "Leave Management" %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
  
    <style>
        header {
            background-color: #1d7484;
            color: white;
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        header a {
            color: white;
            margin-left: 15px;
            font-weight: bold;
            text-decoration: none;
        }
        header a:hover {
            color: #f1f1f1;
            border-bottom: 2px solid #f1f1f1;
        }
        main {
            margin-top: 25px;
            padding: 20px;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .toast {
            position: fixed;
            top: 15px;
            right: 15px;
            background-color: #1d7484;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.2);
            transition: opacity 0.5s;
        }
        .toast.error {
            background-color: #982c61;
        }
    </style>
</head>
<body class="bg-gray-50">

<header>
    <div><strong>🕒 Leave Management</strong></div>
    <nav>
        <a href="${pageContext.request.contextPath}/request/list">Danh sách</a>
        <a href="${pageContext.request.contextPath}/request/create">Tạo mới</a>
        <a href="${pageContext.request.contextPath}/dashboard">Trang chủ</a>
    </nav>
</header>

<!-- Toast Notification -->
<div x-data="{show: true}" x-init="setTimeout(()=>show=false,3000)" 
     x-show="show && ('${param.success}' || '${param.error}')"
     :class="{'toast': true, 'error': '${param.error}' !== ''}">
    <span><%= request.getParameter("success") != null ? request.getParameter("success") : "" %></span>
    <span><%= request.getParameter("error") != null ? request.getParameter("error") : "" %></span>
</div>

<main>

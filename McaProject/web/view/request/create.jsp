<%-- 
    Document   : create
    Created on : Oct 25, 2025, 10:27:27 PM
    Author     : Hiro
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tạo đơn xin nghỉ phép</title>

    <!-- Blocks.css để có hiệu ứng nút, layout hiện đại -->
    <link rel="stylesheet" href="https://unpkg.com/blocks.css/dist/blocks.min.css">

    <style>
        body {
            font-family: "Segoe UI", sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .form-container {
            background-color: #fff;
            border-radius: 16px;
            box-shadow: 0 4px 16px rgba(0,0,0,0.1);
            width: 480px;
            padding: 2rem 2.5rem;
        }

        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 1.5rem;
        }

        form {
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }

        label {
            font-weight: 500;
        }

        input, select, textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 14px;
            resize: vertical;
        }

        textarea {
            min-height: 80px;
        }

        .btn-submit {
            background-color: #4a90e2;
            color: white;
            border: none;
            padding: 10px;
            border-radius: 8px;
            font-size: 15px;
            cursor: pointer;
            transition: 0.2s;
        }

        .btn-submit:hover {
            background-color: #357ab8;
        }

        .back-link {
            text-align: center;
            margin-top: 1rem;
        }

        .back-link a {
            text-decoration: none;
            color: #4a90e2;
            font-weight: 500;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h1>Tạo đơn xin nghỉ phép</h1>

    <form action="${pageContext.request.contextPath}/leave/create" method="post">
        <label for="employeeName">Họ và tên:</label>
        <input type="text" id="employeeName" name="employeeName" placeholder="Nguyễn Văn A" required>

        <label for="department">Phòng ban:</label>
        <input type="text" id="department" name="department" placeholder="Phòng Kỹ thuật" required>

        <label for="startDate">Từ ngày:</label>
        <input type="date" id="startDate" name="startDate" required>

        <label for="endDate">Đến ngày:</label>
        <input type="date" id="endDate" name="endDate" required>

        <label for="reason">Lý do nghỉ:</label>
        <textarea id="reason" name="reason" placeholder="Nhập lý do nghỉ..." required></textarea>

        <label for="leave

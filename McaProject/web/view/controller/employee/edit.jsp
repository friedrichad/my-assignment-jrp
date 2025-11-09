<%-- 
    Document   : edit
    Created on : Nov 6, 2025, 2:27:51 PM
    Author     : Hiro
--%>

<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa nhân viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">

    <style>
        body {
            font-family: "Segoe UI", sans-serif;
            background: #f3f4f6;
            color: #111827;
            margin: 0;
            padding: 40px;
        }

        .edit-container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            padding: 30px 40px;
        }

        h2 {
            text-align: center;
            color: #2563eb;
            margin-bottom: 30px;
        }

        label {
            font-weight: 600;
            display: block;
            margin-bottom: 6px;
            color: #374151;
        }

        input[type="text"],
        input[type="email"],
        select {
            width: 100%;
            padding: 8px 10px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
            margin-bottom: 16px;
            font-size: 14px;
            transition: border 0.2s;
        }

        input:focus, select:focus {
            border-color: #2563eb;
            outline: none;
        }

        .gender-group {
            display: flex;
            gap: 20px;
            margin-bottom: 16px;
        }

        .gender-group label {
            font-weight: 500;
        }

        button {
            background: #2563eb;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 10px 18px;
            cursor: pointer;
            font-size: 15px;
            transition: background 0.3s;
        }

        button:hover {
            background: #1e40af;
        }

        .actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 20px;
        }

        a.cancel {
            color: #6b7280;
            text-decoration: none;
            transition: color 0.3s;
        }

        a.cancel:hover {
            color: #111827;
        }
    </style>
</head>
<body>
<div class="edit-container">
    <h2>✏️ Chỉnh sửa thông tin nhân viên</h2>

    <form action="${pageContext.request.contextPath}/controller/employee/edit" method="post">
        <input type="hidden" name="id" value="${employee.id}">

        <label>Tên nhân viên</label>
        <input type="text" name="name" value="${employee.employeeName}" required>

        <label>Giới tính</label>
        <div class="gender-group">
            <label><input type="radio" name="gender" value="true" ${employee.gender ? "checked" : ""}> Nam</label>
            <label><input type="radio" name="gender" value="false" ${!employee.gender ? "checked" : ""}> Nữ</label>
        </div>

        <label>Email</label>
        <input type="email" name="email" value="${employee.email}" required>

        <label>Phòng ban</label>
        <select name="divisionId" required>
            <option value="">-- Chọn phòng ban --</option>
            <c:forEach var="d" items="${divisions}">
                <option value="${d.id}" ${d.id == employee.division.id ? "selected" : ""}>
                    ${d.divisionName}
                </option>
            </c:forEach>
        </select>

        <label>Người quản lý (Supervisor)</label>
        <select name="supervisorId">
            <option value="">-- Không có --</option>
            <c:forEach var="s" items="${supervisors}">
                <option value="${s.id}" ${s.id == employee.supervisor.id ? "selected" : ""}>
                    ${s.employeeName}
                </option>
            </c:forEach>
        </select>

        <div class="actions">
            <button type="submit">💾 Lưu thay đổi</button>
            <a href="${pageContext.request.contextPath}/controller/dashboard" class="cancel">Hủy</a>
        </div>
    </form>
</div>
</body>
</html>

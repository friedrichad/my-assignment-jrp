<%-- 
    Document   : edit
    Created on : Nov 6, 2025, 2:27:51 PM
    Author     : Hiro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa nhân viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
</head>
<body>
    <h2>Chỉnh sửa thông tin nhân viên</h2>

    <form action="${pageContext.request.contextPath}/controller/employee/edit" method="post">
        <input type="hidden" name="id" value="${employee.id}">

        <label>Tên:</label><br>
        <input type="text" name="name" value="${employee.employeeName}" required><br><br>

        <label>Giới tính:</label><br>
        <label><input type="radio" name="gender" value="male" ${employee.gender ? "checked" : ""}> Nam</label>
        <label><input type="radio" name="gender" value="female" ${!employee.gender ? "checked" : ""}> Nữ</label><br><br>

        <label>Email:</label><br>
        <input type="email" name="email" value="${employee.email}" required><br><br>

        <button type="submit">Lưu thay đổi</button>
        <a href="${pageContext.request.contextPath}/controller/dashboard">Hủy</a>
    </form>
</body>
</html>
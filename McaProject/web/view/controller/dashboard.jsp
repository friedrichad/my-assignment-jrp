<%-- 
    Document   : dashboard
    Created on : Nov 5, 2025, 1:36:33 PM
    Author     : Hiro
--%>

<%-- 
    Dashboard hiển thị danh sách nhân viên thuộc quyền của người đang đăng nhập.
    Có thể thay đổi Division của nhân viên cấp dưới và xem cấu trúc quản lý.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Employee Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 30px;
            }
            h1 {
                text-align: center;
                margin-bottom: 25px;
            }
            form.filter {
                display: flex;
                gap: 10px;
                justify-content: center;
                margin-bottom: 20px;
            }
            table {
                width: 90%;
                margin: auto;
                border-collapse: collapse;
                border: 1px solid #ccc;
            }
            th, td {
                padding: 10px;
                border: 1px solid #ddd;
                text-align: center;
            }
            th {
                background-color: #f3f3f3;
            }
            tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            .pagination {
                text-align: center;
                margin-top: 20px;
            }
            .pagination a {
                display: inline-block;
                padding: 5px 10px;
                margin: 2px;
                border: 1px solid #ccc;
                text-decoration: none;
            }
            .pagination a.active {
                background-color: #007bff;
                color: white;
            }
            .edit-btn {
                background-color: #4CAF50;
                color: white;
                padding: 6px 10px;
                border-radius: 5px;
                text-decoration: none;
            }
            .edit-btn:hover {
                background-color: #45a049;
            }
        </style>
    </head>
    <body>
        <h1>Danh sách nhân viên</h1>

        <!-- Bộ lọc -->
        <form method="get" class="filter">
            <input type="text" name="search" placeholder="Tìm theo tên..." value="${search != null ? search : ''}" />
            <select name="division">
                <option value="">-- Tất cả phòng ban --</option>
                <c:forEach var="d" items="${divisions}">
                    <option value="${d.id}" ${divisionId == d.id ? 'selected' : ''}>${d.divisionName}</option>
                </c:forEach>
            </select>
            <button type="submit">Lọc</button>
        </form>

        <!-- Bảng danh sách -->
        <table>
            <thead>
                <tr>
                    <th>Tên nhân viên</th>
                    <th>Giới tính</th>
                    <th>Ngày sinh</th>
                    <th>Email</th>
                    <th>Phòng ban</th>
                    <th>Supervisor</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="e" items="${employees}">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/controller/employee/edit?id=${e.id}">
                                ${e.employeeName}
                            </a>
                        </td>
                        <td><c:choose>
                                <c:when test="${e.gender}">Nam</c:when>
                                <c:otherwise>Nữ</c:otherwise>
                            </c:choose></td>
                        <td>${e.dob}</td>
                        <td>${e.email}</td>
                        <td>${e.division.divisionName}</td>
                        <td>${e.supervisor.employeeName}</td>
                        <td><a class="edit-btn" href="employee/edit?id=${e.id}">Chỉnh sửa</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Phân trang -->
        <div class="pagination">
            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="?page=${i}&search=${search}&division=${divisionId}" 
                   class="${i == pageIndex ? 'active' : ''}">
                    ${i}
                </a>
            </c:forEach>
        </div>

    </body>
</html>

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
<html lang="vi" x-data>
    <head>
        <meta charset="UTF-8">
        <title>👥 Employee Dashboard</title>

        <!-- CSS chính -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">

        <!-- Alpine.js -->
        <script defer src="${pageContext.request.contextPath}/assets/js/alpine.min.js"></script>

        <style>
            :root {
                --primary: #3b82f6;
                --primary-hover: #2563eb;
                --gray-bg: #f9fafb;
                --border: #e5e7eb;
            }

            body {
                font-family: 'Segoe UI', sans-serif;
                background: var(--gray-bg);
                margin: 0;
                padding: 0;
                color: #333;
            }

            header {
                background: var(--primary);
                color: white;
                padding: 16px 0;
                text-align: center;
                font-size: 1.8em;
                font-weight: bold;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .container {
                max-width: 1100px;
                margin: 30px auto;
                background: white;
                padding: 25px 30px;
                border-radius: 12px;
                box-shadow: 0 4px 10px rgba(0,0,0,0.05);
            }

            form.filter {
                display: flex;
                flex-wrap: wrap;
                gap: 10px;
                justify-content: center;
                margin-bottom: 25px;
            }

            form.filter input,
            form.filter select,
            form.filter button {
                padding: 8px 10px;
                border-radius: 6px;
                border: 1px solid var(--border);
                font-size: 0.95em;
            }

            form.filter button {
                background-color: var(--primary);
                color: white;
                cursor: pointer;
                transition: 0.2s;
            }

            form.filter button:hover {
                background-color: var(--primary-hover);
            }

            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 15px;
            }

            th, td {
                padding: 12px 10px;
                border: 1px solid var(--border);
                text-align: center;
            }

            th {
                background: #f3f4f6;
                text-transform: uppercase;
                font-size: 0.9em;
                letter-spacing: 0.5px;
            }

            tr:nth-child(even) {
                background-color: #f9fafb;
            }

            tr:hover {
                background-color: #f1f5f9;
                transition: 0.2s;
            }

            .edit-btn {
                background-color: var(--primary);
                color: white;
                padding: 6px 12px;
                border-radius: 6px;
                text-decoration: none;
                font-size: 0.9em;
                transition: 0.2s;
            }

            .edit-btn:hover {
                background-color: var(--primary-hover);
            }

            .pagination {
                text-align: center;
                margin-top: 25px;
            }

            .pagination a {
                display: inline-block;
                padding: 8px 12px;
                margin: 2px;
                border-radius: 6px;
                text-decoration: none;
                border: 1px solid var(--border);
                color: #333;
                transition: 0.2s;
            }

            .pagination a.active {
                background-color: var(--primary);
                color: white;
                border-color: var(--primary);
            }

            .pagination a:hover {
                background-color: var(--primary-hover);
                color: white;
            }

            @media (max-width: 768px) {
                table {
                    font-size: 0.85em;
                }

                th, td {
                    padding: 8px;
                }

                form.filter {
                    flex-direction: column;
                    align-items: center;
                }
            }
        </style>
    </head>

    <body>
        <header>Danh sách nhân viên 👥</header>

        <div class="container" x-data="{ search: '${search != null ? search : ""}', division: '${divisionId != null ? divisionId : ""}' }">

            <!-- Bộ lọc -->
            <form method="get" class="filter">
                <input type="text" name="search" placeholder="🔍 Tìm theo tên..." 
                       x-model="search"
                       :value="search"
                       @input.debounce.300ms="$el.form.submit()">

                <select name="division" x-model="division" @change="$el.form.submit()">
                    <option value="">-- Tất cả phòng ban --</option>
                    <c:forEach var="d" items="${divisions}">
                        <option value="${d.id}" ${divisionId == d.id ? 'selected' : ''}>${d.divisionName}</option>
                    </c:forEach>
                </select>
                <button type="submit">Lọc</button>
            </form>

            <!-- Bảng danh sách -->
            <div class="table-wrapper">
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
                                <td>
                                    <c:choose>
                                        <c:when test="${e.gender}">Nam</c:when>
                                        <c:otherwise>Nữ</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${e.dob}</td>
                                <td>${e.email}</td>
                                <td>${e.division.divisionName}</td>
                                <td>${e.supervisor.employeeName}</td>
                                <td><a class="edit-btn" href="employee/edit?id=${e.id}">Chỉnh sửa</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Phân trang -->
            <div class="pagination">
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <a href="?page=${i}&search=${search}&division=${divisionId}" 
                       class="${i == pageIndex ? 'active' : ''}">
                        ${i}
                    </a>
                </c:forEach>
            </div>

        </div>
    </body>
</html>
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

<style>
    /* ========== DASHBOARD LAYOUT ========== */
    .dashboard-container {
        display: flex;
        flex-direction: column;
        gap: 1.5rem;
        width: 100%;
        height: 100%;
        box-sizing: border-box;
        padding: 1.5rem 2rem;
        max-width: 1200px;
        margin: 0 auto;
    }

    /* HEADER */
    .dashboard-header {
        font-size: 1.75rem;
        font-weight: 700;
        color: #1e3a8a;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .dashboard-header span {
        font-size: 1.8rem;
    }

    /* FILTER BAR */
    .filter {
        display: flex;
        flex-wrap: wrap;
        gap: 0.75rem;
        align-items: center;
        background: #ffffff;
        padding: 1rem 1.25rem;
        border-radius: 10px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.08);
        border: 1px solid #e5e7eb;
    }

    .filter input[type="text"],
    .filter select {
        padding: 0.6rem 0.9rem;
        border: 1px solid #d1d5db;
        border-radius: 8px;
        font-size: 0.95rem;
        flex: 1;
        min-width: 180px;
    }

    .filter input:focus,
    .filter select:focus {
        border-color: #2563eb;
        box-shadow: 0 0 0 3px rgba(37,99,235,0.1);
        outline: none;
    }

    .filter button {
        background: #2563eb;
        color: white;
        border: none;
        padding: 0.6rem 1rem;
        border-radius: 8px;
        font-weight: 600;
        cursor: pointer;
        transition: background 0.2s;
    }

    .filter button:hover {
        background: #1d4ed8;
    }

    /* TABLE WRAPPER */
    .table-wrapper {
        background: #ffffff;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        border: 1px solid #e5e7eb;
        overflow: hidden;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        font-size: 0.95rem;
    }

    thead {
        background: #f3f4f6;
    }

    th, td {
        padding: 0.9rem 1rem;
        border-bottom: 1px solid #e5e7eb;
        text-align: left;
        vertical-align: middle;
    }

    th {
        text-transform: uppercase;
        font-weight: 600;
        font-size: 0.8rem;
        letter-spacing: 0.5px;
        color: #374151;
    }

    tr:hover td {
        background-color: #f9fafb;
    }

    a.employee-link {
        color: #2563eb;
        text-decoration: none;
        font-weight: 500;
    }
    a.employee-link:hover { text-decoration: underline; }

    .edit-btn {
        background-color: #2563eb;
        color: white;
        padding: 0.4rem 0.8rem;
        border-radius: 6px;
        text-decoration: none;
        font-size: 0.85rem;
        font-weight: 500;
        transition: background 0.2s;
    }
    .edit-btn:hover {
        background-color: #1e40af;
    }

    /* PAGINATION */
    .pagination {
        display: flex;
        justify-content: center;
        gap: 0.4rem;
        flex-wrap: wrap;
        padding: 1rem 0;
    }

    .pagination a {
        display: inline-block;
        padding: 0.45rem 0.9rem;
        border: 1px solid #d1d5db;
        border-radius: 8px;
        text-decoration: none;
        color: #374151;
        font-weight: 500;
        transition: 0.2s;
    }

    .pagination a.active {
        background-color: #2563eb;
        color: #ffffff;
        border-color: #2563eb;
        font-weight: 600;
    }

    .pagination a:hover {
        background-color: #1e40af;
        color: white;
    }

    /* RESPONSIVE */
    @media (max-width: 768px) {
        .dashboard-container {
            padding: 1rem;
        }
        .filter {
            flex-direction: column;
            align-items: stretch;
        }
        th, td { font-size: 0.85rem; }
    }
</style>

<div class="dashboard-container" 
     x-data="{ search: '${search != null ? search : ""}', division: '${divisionId != null ? divisionId : ""}' }">

    <!-- Header -->
    <div class="dashboard-header">
        <span>👥</span> Danh sách nhân viên
    </div>

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
                    <th>Mã NV</th>
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
                        <td>${e.id}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/controller/employee/edit?id=${e.id}" 
                               class="employee-link">${e.employeeName}</a>
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

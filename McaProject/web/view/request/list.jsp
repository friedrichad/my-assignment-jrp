<%-- 
    Document   : list.jsp
    Created on : Oct 30, 2025
    Author     : Hiro
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.LeaveRequest" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- ================== CSS ================== -->
<style>
    header.list-header {
        background: #1d7484;
        color: white;
        padding: 1rem 2rem;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-radius: 6px;
    }

    header.list-header h1 {
        margin: 0;
        font-size: 1.3rem;
    }

    .filter-form {
        display: flex;
        flex-wrap: wrap;
        gap: 0.5rem;
        background: white;
        padding: 1rem 2rem;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        align-items: center;
        margin-top: 15px;
        border-radius: 6px;
    }

    .filter-form label {
        font-weight: 600;
        margin-right: 5px;
    }

    .filter-form input, .filter-form select, .filter-form button {
        padding: 6px 10px;
        border-radius: 6px;
        border: 1px solid #ccc;
        font-size: 14px;
    }

    .filter-form button {
        background: #1d7484;
        color: white;
        border: none;
        cursor: pointer;
    }

    .filter-form button:hover {
        background: #125866;
    }

    .status-badge {
        display: inline-block;
        padding: 4px 8px;
        border-radius: 6px;
        font-weight: 600;
        font-size: 13px;
        text-transform: capitalize;
    }

    .status-badge.Pending { background: #fff3cd; color: #856404; }
    .status-badge.Approved { background: #d4edda; color: #155724; }
    .status-badge.Rejected { background: #f8d7da; color: #721c24; }

    table {
        width: 100%;
        border-collapse: collapse;
        font-size: 14px;
        margin-top: 1rem;
        background: white;
    }

    th, td {
        border: 1px solid #e0e0e0;
        padding: 10px;
        text-align: left;
        white-space: nowrap;
    }

    th {
        background: #e3f2fd;
        font-weight: bold;
        position: sticky;
        top: 0;
        z-index: 1;
    }

    tr:hover {
        background: #f1f8ff;
    }

    .btn-edit {
        background: #ffc107;
        color: #212529;
        padding: 4px 8px;
        border-radius: 4px;
        text-decoration: none;
        font-weight: 500;
    }

    .btn-edit:hover {
        background: #e0a800;
        color: white;
    }

    .btn-delete {
        background: #dc3545;
        color: white;
        padding: 4px 8px;
        border-radius: 4px;
        text-decoration: none;
        font-weight: 500;
        margin-left: 5px;
    }

    .btn-delete:hover {
        background: #c82333;
        color: #fff;
    }

    .message {
        margin: 10px 2rem;
        padding: 10px 15px;
        border-radius: 6px;
        text-align: center;
        font-weight: 500;
    }

    .message.success {
        background: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }

    .message.error {
        background: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }

    .remaining {
        margin: 10px 2rem;
        font-size: 15px;
        font-weight: 500;
    }

    .remaining.good { color: green; }
    .remaining.bad { color: red; }

    .pagination {
        text-align: center;
        margin: 20px 0;
    }

    .pagination a {
        margin: 0 5px;
        text-decoration: none;
        color: #1d7484;
    }

    .pagination a.active {
        font-weight: bold;
        color: #982c61;
    }
</style>

<!-- ================== NỘI DUNG TRANG ================== -->
<div x-data="{ showMsg: true }">
    <header class="list-header">
        <h1>📅 Danh sách đơn nghỉ phép</h1>
        <a href="${pageContext.request.contextPath}/request/create" 
           style="background:white;color:#1d7484;padding:8px 14px;border-radius:6px;text-decoration:none;">
           ➕ Tạo đơn
        </a>
    </header>

    <!-- Thông báo -->
    <c:if test="${not empty success}">
        <div class="message success" x-show="showMsg" x-init="setTimeout(() => showMsg = false, 3000)">
            ✅ ${success}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="message error" x-show="showMsg" x-init="setTimeout(() => showMsg = false, 3000)">
            ⚠️ ${error}
        </div>
    </c:if>

    <!-- Bộ lọc -->
    <form method="get" action="${pageContext.request.contextPath}/request/list" class="filter-form">
        <label>Từ ngày:</label>
        <input type="date" name="from" value="${fromDate}">

        <label>Đến ngày:</label>
        <input type="date" name="to" value="${toDate}">

        <label>Trạng thái:</label>
        <select name="status" onchange="this.form.submit()">
            <option value="">--Tất cả--</option>
            <option value="Pending" ${statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
            <option value="Approved" ${statusFilter == 'Approved' ? 'selected' : ''}>Approved</option>
            <option value="Rejected" ${statusFilter == 'Rejected' ? 'selected' : ''}>Rejected</option>
        </select>

        <button type="submit">🔍 Lọc</button>
    </form>

    <!-- Ngày phép còn lại -->
    <c:choose>
        <c:when test="${remainingDays > 0}">
            <p class="remaining good">✅ Bạn còn ${remainingDays} ngày nghỉ phép.</p>
        </c:when>
        <c:otherwise>
            <p class="remaining bad">🚫 Bạn đã hết số ngày nghỉ phép cho phép.</p>
        </c:otherwise>
    </c:choose>

    <!-- Bảng dữ liệu -->
    <table>
        <tr>
            <th>ID</th>
            <th>Từ ngày</th>
            <th>Đến ngày</th>
            <th>Số ngày</th>
            <th>Trạng thái</th>
            <th>Lý do</th>
            <th>Ngày tạo</th>
            <th>Thao tác</th>
        </tr>
        <c:forEach var="r" items="${requests}">
            <tr>
                <td>${r.id}</td>
                <td>${r.startDate}</td>
                <td>${r.endDate}</td>
                <td>${r.numDays}</td>
                <td><span class="status-badge ${r.status}">${r.status}</span></td>
                <td>${r.reason}</td>
                <td>${r.requestedAt}</td>
                <td>
                    <c:if test="${r.status == 'Pending'}">
                        <a href="${pageContext.request.contextPath}/request/edit?id=${r.id}" class="btn-edit">✏️ Sửa</a>
                        <a href="${pageContext.request.contextPath}/request/delete?action=delete&id=${r.id}" 
                           class="btn-delete"
                           onclick="return confirm('Bạn có chắc muốn xóa đơn nghỉ phép này không?');">
                           🗑️ Xóa
                        </a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!-- Phân trang -->
    <div class="pagination">
        <c:forEach var="i" begin="1" end="${totalPages}">
            <a href="?page=${i}&size=${size}&status=${statusFilter}&from=${fromDate}&to=${toDate}"
               class="${i == page ? 'active' : ''}">
                ${i}
            </a>
        </c:forEach>
    </div>
</div>

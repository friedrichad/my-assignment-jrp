<%-- 
    Document   : list
    Created on : Oct 30, 2025, 1:19:02 PM
    Author     : Hiro
--%>

<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.util.*, model.LeaveRequest"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Danh sách đơn nghỉ phép</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">

        <style>
            html, body {
                margin: 0;
                padding: 0;
                max-width: none !important;
                min-height: 100vh;
                font-family: "Segoe UI", Tahoma, sans-serif;
                background: #fff;
            }

            h1 {
                color: white;
                padding: 10px 20px;
                margin: 0;
                font-size: 20px;
                text-align: left;
            }

            .table-container {
                width: 100%;
                height: calc(100vh - 120px); /* trừ header + phân trang */
                overflow: auto;
            }

            table {
                border-collapse: collapse;
                width: 100%;
                font-size: 14px;
            }

            th, td {
                border: 1px solid #ccc;
                padding: 8px 12px;
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

            tr:nth-child(even) {
                background: #f9f9f9;
            }
            tr:hover {
                background: #dbeafe;
            }

            .btn-edit {
                background: #ffc107;
                border: none;
                color: white;
                padding: 4px 8px;
                border-radius: 4px;
                cursor: pointer;
                text-decoration: none;
            }

            .btn-edit[disabled] {
                background: #ccc;
                cursor: not-allowed;
            }

            .status.Pending {
                color: #ff9800;
                font-weight: bold;
            }
            .status.Approved {
                color: #4caf50;
                font-weight: bold;
            }
            .status.Rejected {
                color: #f44336;
                font-weight: bold;
            }

            .back {
                display: inline-block;
                margin: 10px 20px;
                color: white;
                padding: 8px 14px;
                border-radius: 6px;
                text-decoration: none;
                font-size: 14px;
            }

            .pagination {
                height: 40px;
                padding: 10px 20px;
                text-align: center;
                background: #f0f2f5;
            }

            .message {
                padding: 10px 20px;
                border-radius: 6px;
                text-align: center;
                font-weight: 500;
                margin: 10px 20px;
            }

            .message.error {
                background: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }

            .message.success {
                background: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }
            .headContainer{
                display: flex;
                justify-content: space-between;
                align-items: center;
                background: #4a90e2;
            }
        </style>
    </head>
    <body>
        <div class="headContainer">
            <h2>📅 Danh sách đơn nghỉ phép</h2>

<form method="get" action="${pageContext.request.contextPath}/request/list">
    <label>Từ ngày:</label> <input type="date" name="from" value="${fromDate}">
    <label>Đến ngày:</label> <input type="date" name="to" value="${toDate}">
    <label>Trạng thái:</label>
    <select name="status">
        <option value="">--Tất cả--</option>
        <option value="Pending" ${statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
        <option value="Approved" ${statusFilter == 'Approved' ? 'selected' : ''}>Approved</option>
        <option value="Rejected" ${statusFilter == 'Rejected' ? 'selected' : ''}>Rejected</option>
    </select>
    <button type="submit">🔍 Lọc</button>
</form>

<c:if test="${remainingDays > 0}">
    <p style="color:green;">✅ Bạn còn ${remainingDays} ngày nghỉ phép.</p>
</c:if>
<c:if test="${remainingDays <= 0}">
    <p style="color:red;">🚫 Bạn đã nghỉ hết số buổi cho phép (99 ngày).</p>
</c:if>

<table border="1" cellspacing="0" cellpadding="6">
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
            <td>${r.status}</td>
            <td>${r.reason}</td>
            <td>${r.requestedAt}</td>
            <td>
                <c:if test="${r.status == 'Pending'}">
                    <a href="${pageContext.request.contextPath}/request/edit?id=${r.id}">✏️ Sửa</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

<!-- Phân trang -->
<div style="margin-top:10px;">
    <c:forEach var="i" begin="1" end="${totalPages}">
        <a href="?page=${i}&size=${size}&status=${statusFilter}&from=${fromDate}&to=${toDate}"
           style="margin-right:5px; ${i == page ? 'font-weight:bold;' : ''}">
            ${i}
        </a>
    </c:forEach>
</div>


    </body>
</html>

<%-- 
    Document   : list
    Created on : Oct 30, 2025, 1:19:02 PM
    Author     : Hiro
--%>

<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.util.*, model.LeaveRequest"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách đơn nghỉ phép</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">

    <style>
        body {
            background: #f0f2f5;
            padding: 40px;
        }

        h1 {
            text-align: center;
            margin-bottom: 30px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }

        th, td {
            padding: 12px 16px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        th {
            background: #4a90e2;
            color: white;
        }

        tr:hover {
            background: #f7faff;
        }

        .btn-edit {
            background: #ffc107;
            border: none;
            color: white;
            padding: 6px 12px;
            border-radius: 6px;
            cursor: pointer;
            text-decoration: none;
        }

        .btn-edit[disabled] {
            background: #ccc;
            cursor: not-allowed;
        }

        .back {
            display: block;
            margin-top: 20px;
            text-align: center;
        }
    </style>
</head>
<body>

<h1>📄 Danh sách đơn nghỉ phép</h1>

<table>
    <thead>
        <tr>
            <th>Mã đơn</th>
            <th>Loại nghỉ</th>
            <th>Từ ngày</th>
            <th>Đến ngày</th>
            <th>Số ngày</th>
            <th>Lý do</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
        </tr>
    </thead>
    <tbody>
        <%
            ArrayList<LeaveRequest> requests = (ArrayList<LeaveRequest>) request.getAttribute("requests");
            if (requests != null && !requests.isEmpty()) {
                for (LeaveRequest lr : requests) {
        %>
        <tr>
            <td><%= lr.getId() %></td>
            <td><%= lr.getTypeid() %></td>
            <td><%= lr.getStartDate() %></td>
            <td><%= lr.getEndDate() %></td>
            <td><%= lr.getNumDays() %></td>
            <td><%= lr.getReason() %></td>
            <td><%= lr.getStatus() != null ? lr.getStatus() : "Pending" %></td>
            <td>
                <% if (lr.getStatus() == null || "Pending".equalsIgnoreCase(lr.getStatus())) { %>
                    <a href="${pageContext.request.contextPath}/request/edit?id=<%= lr.getId() %>" class="btn-edit">Sửa</a>
                <% } else { %>
                    <button class="btn-edit" disabled>Đã duyệt</button>
                <% } %>
            </td>
        </tr>
        <% 
                }
            } else { 
        %>
        <tr><td colspan="8" style="text-align:center;">Chưa có đơn nghỉ phép nào.</td></tr>
        <% } %>
    </tbody>
</table>

<div class="back">
    <a href="${pageContext.request.contextPath}/request/create">➕ Tạo đơn mới</a>
</div>

</body>
</html>

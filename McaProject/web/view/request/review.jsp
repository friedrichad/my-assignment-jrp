<%-- 
    Document   : review
    Created on : Nov 4, 2025, 9:21:59 AM
    Author     : Hiro
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.LeaveRequest" %>
<%@ page import="model.Employee" %>
<%@ page import="model.LeaveType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Duyệt đơn nghỉ phép</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">

    <style>
        body {
            font-family: "Segoe UI", Tahoma, sans-serif;
            background: #f4f6f8;
            margin: 0;
            padding: 20px;
            max-width: none !important;
        }

        h2 {
            color: #333;
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
            border-radius: 8px;
            overflow: hidden;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 10px 12px;
            text-align: center;
            white-space: nowrap;
        }

        th {
            background: #007bff;
            color: white;
            position: sticky;
            top: 0;
        }

        tr:nth-child(even) { background: #f9f9f9; }
        tr:hover { background: #dbeafe; }

        .btn {
            padding: 6px 12px;
            border: none;
            border-radius: 5px;
            color: white;
            cursor: pointer;
            text-decoration: none;
        }

        .approve { background-color: #28a745; }
        .reject { background-color: #dc3545; }
        .btn:hover { opacity: 0.9; }

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

        .note {
            margin-top: 15px;
            font-style: italic;
            color: #555;
            text-align: center;
        }
    </style>
</head>
<body>

<h2>📋 Duyệt đơn nghỉ phép</h2>

<%
    ArrayList<LeaveRequest> requests = (ArrayList<LeaveRequest>) request.getAttribute("requests");
    if (requests == null || requests.isEmpty()) {
%>
    <p style="text-align:center; color:#666;">Không có đơn nghỉ phép nào cần duyệt.</p>
<%
    } else {
%>

<table>
    <thead>
    <tr>
        <th>Mã đơn</th>
        <th>Nhân viên</th>
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
        for (LeaveRequest lr : requests) {
            String status = lr.getStatus() != null ? lr.getStatus() : "Pending";
    %>
    <tr>
        <td><%= lr.getId() %></td>
        <td><%= lr.getEmployee() != null ? lr.getEmployee().getEmployeeName() : "N/A" %></td>
        <td><%= lr.getLeaveTypeName() %></td>
        <td><%= lr.getStartDate() %></td>
        <td><%= lr.getEndDate() %></td>
        <td><%= lr.getNumDays() %></td>
        <td><%= lr.getReason() %></td>
        <td class="status <%= status %>"><%= status %></td>
        <td>
            <% if ("Pending".equalsIgnoreCase(status)) { %>
                <form action="review" method="post" style="display:inline;">
                    <input type="hidden" name="reqid" value="<%= lr.getId() %>">
                    <input type="hidden" name="action" value="approve">
                    <button type="submit" class="btn approve">Approve</button>
                </form>
                <form action="review" method="post" style="display:inline;">
                    <input type="hidden" name="reqid" value="<%= lr.getId() %>">
                    <input type="hidden" name="action" value="reject">
                    <button type="submit" class="btn reject">Reject</button>
                </form>
            <% } else { %>
                <button class="btn" style="background:#ccc; cursor:default;"><%= status %></button>
            <% } %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>

<% } %>

<p class="note">Bạn có thể phê duyệt hoặc từ chối các đơn nghỉ phép của cấp dưới tại đây.</p>

</body>
</html>
<%-- 
    Document   : list
    Created on : Oct 30, 2025, 1:19:02 PM
    Author     : Hiro
--%>

<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.util.*, model.LeaveRequest"%>


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
            <h1>📄 Danh sách đơn nghỉ phép</h1>
        <a href="${pageContext.request.contextPath}/request/create" class="back">➕ Tạo đơn mới</a>
        </div>


        <%
        ArrayList<LeaveRequest> requests = (ArrayList<LeaveRequest>) request.getAttribute("requests");
        String errorMsg = request.getParameter("error");
        String successMsg = request.getParameter("success");
        if (errorMsg == null) errorMsg = (String) request.getAttribute("error");
        if (successMsg == null) successMsg = (String) request.getAttribute("success");
        %>

        <% if (errorMsg != null) { %>
        <div class="message error">⚠️ <%= errorMsg %></div>
        <% } else if (successMsg != null) { %>
        <div class="message success">✅ <%= successMsg %></div>
        <% } %>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>Mã đơn</th>
                        <th>Tên nhân viên</th>
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
                        if (requests != null && !requests.isEmpty()) {
                            for (LeaveRequest lr : requests) {
                    %>
                    <tr>
                        <td><%= lr.getReqid() %></td>
                        <td><%= lr.getEmployee() != null ? lr.getEmployee().getEmployeeName() : "N/A" %></td>
                        <td><%= lr.getTypeid() %></td>
                        <td><%= lr.getStartDate() %></td>
                        <td><%= lr.getEndDate() %></td>
                        <td><%= lr.getNumDays() %></td>
                        <td><%= lr.getReason() %></td>
                        <td class="status <%= lr.getStatus() %>"><%= lr.getStatus() != null ? lr.getStatus() : "Pending" %></td>
                        <td>
                            <% if (lr.getStatus() == null || "Pending".equalsIgnoreCase(lr.getStatus())) { %>
                            <a href="${pageContext.request.contextPath}/request/edit?id=<%= lr.getReqid() %>" class="btn-edit">Sửa</a>
                            <% } else { %>
                            <button class="btn-edit" disabled>Đã duyệt</button>
                            <% } %>
                        </td>
                    </tr>
                    <% } } else { %>
                    <tr><td colspan="9" style="text-align:center;">Chưa có đơn nghỉ phép nào.</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <div class="pagination">
            <!-- Nút phân trang giả lập -->
            <a href="#">« Trước</a> | <a href="#">1</a> | <a href="#">2</a> | <a href="#">Tiếp »</a>
        </div>


    </body>
</html>

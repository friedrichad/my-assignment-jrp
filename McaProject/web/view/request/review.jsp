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
<html>
<head>
    <title>Review Leave Requests</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f8; margin: 0; padding: 20px; }
        h2 { color: #333; text-align: center; }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: white;
            border-radius: 8px;
            overflow: hidden;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 10px 12px;
            text-align: center;
        }
        th { background: #007bff; color: white; }
        tr:nth-child(even) { background: #f9f9f9; }
        tr:hover { background: #eef3ff; }
        .btn {
            padding: 6px 12px;
            border: none;
            border-radius: 5px;
            color: white;
            cursor: pointer;
        }
        .approve { background-color: #28a745; }
        .reject { background-color: #dc3545; }
        .btn:hover { opacity: 0.9; }
        .note {
            margin-top: 15px;
            font-style: italic;
            color: #555;
        }
    </style>
</head>
<body>

<h2>Review Leave Requests</h2>

<%
    ArrayList<LeaveRequest> requests = (ArrayList<LeaveRequest>) request.getAttribute("requests");
    if (requests == null || requests.isEmpty()) {
%>
    <p style="text-align:center; color:#666;">No leave requests pending review.</p>
<%
    } else {
%>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Employee</th>
        <th>Type</th>
        <th>Start</th>
        <th>End</th>
        <th>Days</th>
        <th>Reason</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (LeaveRequest lr : requests) {
    %>
    <tr>
        <td><%= lr.getId() %></td>
        <td><%= lr.getEmployee() != null ? lr.getEmployee().getEmployeeName() : "N/A" %></td>
        <td><%= lr.getLeaveTypeName() %></td>
        <td><%= lr.getStartDate() %></td>
        <td><%= lr.getEndDate() %></td>
        <td><%= lr.getNumDays() %></td>
        <td><%= lr.getReason() %></td>
        <td><%= lr.getStatus() %></td>
        <td>
            <% if ("Pending".equalsIgnoreCase(lr.getStatus())) { %>
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
                <%= lr.getStatus() %>
            <% } %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>

<% } %>

<p class="note">You can approve or reject pending leave requests from your subordinates here.</p>

</body>
</html>

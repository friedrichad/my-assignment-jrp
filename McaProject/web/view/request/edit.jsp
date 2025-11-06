<%-- 
    Document   : edit
    Created on : Nov 3, 2025, 2:19:29 PM
    Author     : Hiro
--%>

<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="model.LeaveRequest, java.util.ArrayList, model.LeaveType"%>
<%
    LeaveRequest lr = (LeaveRequest) request.getAttribute("request");
    ArrayList<LeaveType> leaveTypes = (ArrayList<LeaveType>) request.getAttribute("leaveTypes");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>✏️ Chỉnh sửa đơn nghỉ phép</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
</head>
<body>
<h1>✏️ Chỉnh sửa đơn nghỉ phép</h1>

<form action="${pageContext.request.contextPath}/request/edit" method="post">
    <input type="hidden" name="reqid" value="<%= lr.getId() %>">

    <label>Loại nghỉ:</label><br>
    <select name="typeid" required>
        <% for (LeaveType t : leaveTypes) { %>
            <option value="<%= t.getId() %>" <%= (t.getId() == lr.getTypeid()) ? "selected" : "" %>>
                <%= t.getTypename() %>
            </option>
        <% } %>
    </select><br><br>

    <label>Từ ngày:</label>
    <input type="date" name="startDate" value="<%= lr.getStartDate() %>" required><br><br>

    <label>Đến ngày:</label>
    <input type="date" name="endDate" value="<%= lr.getEndDate() %>" required><br><br>

    <label>Lý do:</label><br>
    <textarea name="reason" rows="4" cols="40"><%= lr.getReason() %></textarea><br><br>

    <button type="submit">💾 Cập nhật</button>
    <a href="${pageContext.request.contextPath}/request/list">⬅ Quay lại</a>
</form>

</body>
</html>
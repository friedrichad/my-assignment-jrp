<%-- 
    Document   : edit
    Created on : Nov 3, 2025, 2:19:29 PM
    Author     : Hiro
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.LeaveRequest, java.util.ArrayList, model.LeaveType" %>
<%
    LeaveRequest lr = (LeaveRequest) request.getAttribute("request");
    ArrayList<LeaveType> leaveTypes = (ArrayList<LeaveType>) request.getAttribute("leaveTypes");
%>

<div class="edit-container">
    <h1>✏️ Chỉnh sửa đơn nghỉ phép</h1>

    <form action="${pageContext.request.contextPath}/request/edit" method="post">
        <input type="hidden" name="reqid" value="<%= lr.getId() %>">

        <label for="typeid">Loại nghỉ phép:</label>
        <select id="typeid" name="typeid" required>
            <% for (LeaveType t : leaveTypes) { %>
                <option value="<%= t.getId() %>" <%= (t.getId() == lr.getTypeid()) ? "selected" : "" %>>
                    <%= t.getTypename() %>
                </option>
            <% } %>
        </select>

        <label for="startDate">Từ ngày:</label>
        <input type="date" id="startDate" name="startDate" value="<%= lr.getStartDate() %>" required>

        <label for="endDate">Đến ngày:</label>
        <input type="date" id="endDate" name="endDate" value="<%= lr.getEndDate() %>" required>

        <label for="reason">Lý do nghỉ:</label>
        <textarea id="reason" name="reason" placeholder="Nhập lý do nghỉ..." required><%= lr.getReason() %></textarea>

        <div class="button-group">
            <button type="submit" class="btn-submit">💾 Cập nhật</button>
            <a href="${pageContext.request.contextPath}/request/list" class="btn-back">⬅ Quay lại</a>
        </div>
    </form>
</div>

<style>
/* ✅ Form edit nằm trong layout main, nên chỉ cần chỉnh kích thước và màu nền */
.edit-container {
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    padding: 2rem 2.5rem;
    margin: 1.5rem auto;
    width: 100%;
    max-width: 650px;
    display: flex;
    flex-direction: column;
    gap: 1rem;
    animation: fadeIn 0.4s ease;
}

h1 {
    text-align: center;
    color: #1d7484;
    margin-bottom: 1rem;
    font-size: 1.6rem;
}

label {
    font-weight: 600;
    margin-bottom: 0.4rem;
}

input, select, textarea {
    width: 100%;
    padding: 10px 14px;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #f9f9f9;
    font-size: 1rem;
    transition: border 0.2s, box-shadow 0.2s;
}

input:focus, select:focus, textarea:focus {
    border: 1px solid #1d7484;
    box-shadow: 0 0 5px rgba(29,116,132,0.3);
    outline: none;
}

textarea {
    resize: vertical;
    min-height: 120px;
}

.button-group {
    display: flex;
    gap: 0.8rem;
    margin-top: 0.5rem;
}

.btn-submit {
    flex: 1;
    background-color: #1d7484;
    color: white;
    border: none;
    padding: 12px;
    border-radius: 8px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.2s;
}

.btn-submit:hover {
    background-color: #982c61;
}

.btn-back {
    flex: 1;
    background-color: #ccc;
    color: #333;
    text-align: center;
    text-decoration: none;
    border-radius: 8px;
    padding: 12px;
    font-weight: 500;
    transition: background-color 0.2s;
}

.btn-back:hover {
    background-color: #bbb;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(-8px); }
    to { opacity: 1; transform: translateY(0); }
}
</style>

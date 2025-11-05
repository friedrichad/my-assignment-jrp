<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.LeaveType" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Tạo đơn xin nghỉ phép</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">

        <style>
            body {
                background-color: #f0f2f5;
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
                margin: 0;
                max-width: none !important;
            }

            .form-container {
                background-color: #fff;
                border-radius: 16px;
                box-shadow: 0 4px 16px rgba(0,0,0,0.1);
                width: 700px;
                padding: 2rem 2.5rem;
                display: flex;
                flex-direction: column;
                gap: 1.5rem;
            }

            h1 {
                text-align: center;
                color: #333;
                margin-bottom: 0.5rem;
            }

            form {
                display: flex;
                flex-direction: column;
                gap: 1rem;
            }

            label {
                font-weight: 500;
            }

            input, select, textarea {
                width: 100%;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 8px;
                font-size: 14px;
                resize: vertical;
            }

            textarea {
                min-height: 80px;
            }

            .btn-submit {
                background-color: #4a90e2;
                color: white;
                border: none;
                padding: 10px;
                border-radius: 8px;
                font-size: 15px;
                cursor: pointer;
                transition: 0.2s;
            }

            .btn-submit:hover {
                background-color: #357ab8;
            }

            .back-link {
                text-align: center;
                margin-top: 1rem;
            }

            .back-link a {
                text-decoration: none;
                color: #4a90e2;
                font-weight: 500;
            }
        </style>
    </head>
    <body>

        <div class="form-container">
            <h1>Tạo đơn xin nghỉ phép</h1>

            <form action="${pageContext.request.contextPath}/request/create" method="post">
                <label for="leaveType">Loại nghỉ phép:</label>
                <select id="leaveType" name="typeId" required>
                    <option value="" disabled selected>Chọn loại nghỉ</option>
                    <% 
                        ArrayList<LeaveType> leaveTypes = (ArrayList<LeaveType>) request.getAttribute("leaveTypes");
                        if (leaveTypes != null) {
                            for (LeaveType lt : leaveTypes) {
                    %>
                    <option value="<%= lt.getId() %>"><%= lt.getTypename() %></option>
                    <% 
                            }
                        }
                    %>
                </select>

                <label for="startDate">Từ ngày:</label>
                <input type="date" id="startDate" name="startDate" required>

                <label for="endDate">Đến ngày:</label>
                <input type="date" id="endDate" name="endDate" required>

                <label for="reason">Lý do nghỉ:</label>
                <textarea id="reason" name="reason" placeholder="Nhập lý do nghỉ..." required></textarea>

                <button type="submit" class="btn-submit">Gửi Đơn</button>
            </form>
            <c:if test="${not empty error}">
                <div style="color: red; font-weight: bold; text-align: center; margin-top: 0.5rem;">
                    ${error}
                </div>
            </c:if>

            <div class="back-link">
                <a href="${pageContext.request.contextPath}/request/list">Quay lại danh sách</a>
            </div>
        </div>

    </body>
</html>

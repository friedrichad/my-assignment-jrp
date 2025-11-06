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
            html, body {
                margin: 0;
                padding: 0;
                width: 100%;
                height: 100%;
                font-size: 1.1rem;
                background-color: #f0f2f5;
                max-width: none !important;

            }

            body {
                display: flex;
                flex-direction: column;
                width: 100vw;
                height: 100vh;
            }

            .container {
                display: flex;
                flex: 1 1 auto;
                width: 100%;
                height: 100%;
            }

            .form-box, .info-box {
                flex: 1 1 0;
                background-color: #fff;
                border-radius: 0;
                box-shadow: none;
                padding: 2rem;
                display: flex;
                flex-direction: column;
                overflow-y: auto;
            }

            h1, h2 {
                margin-top: 0;
                color: #1d7484;
                text-align: center;
            }

            form {
                display: flex;
                flex-direction: column;
                gap: 1rem;
            }

            label {
                font-weight: 600;
            }

            input, select, textarea {
                width: 100%;
                padding: 12px 15px;
                font-size: 1rem;
                border: 1px solid #ddd;
                border-radius: 8px;
                background-color: #f9f9f9;
                box-sizing: border-box;
                transition: border 0.2s, box-shadow 0.2s;
            }

            input:focus, select:focus, textarea:focus {
                border: 1px solid #1d7484;
                outline: none;
                box-shadow: 0 0 5px rgba(29,116,132,0.3);
            }

            textarea {
                min-height: 150px;
                resize: vertical;
            }

            .btn-submit {
                background-color: #1d7484;
                color: white;
                border: none;
                padding: 14px;
                border-radius: 8px;
                font-size: 1.1rem;
                cursor: pointer;
                transition: background-color 0.2s;
            }

            .btn-submit:hover {
                background-color: #982c61;
            }

            .error {
                color: red;
                font-weight: bold;
                text-align: center;
                margin-top: 0.5rem;
            }

            .info-box {
                line-height: 1.6;
                padding: 2rem;
                background-color: #eef7f9;
            }

            .info-box ul {
                padding-left: 1.4em;
            }

            /* Responsive */
            @media (max-width: 1200px) {
                .container {
                    flex-direction: column;
                }
                .form-box, .info-box {
                    height: 50%;
                }
            }
        </style>
    </head>
    <body>

        <div class="container">

            <!-- Form điền đơn -->
            <div class="form-box">
                <h1>Tạo đơn xin nghỉ phép</h1>
                <form action="${pageContext.request.contextPath}/request/create" method="post">
                    <label for="leaveType">Loại nghỉ phép:</label>
                    <select id="leaveType" name="typeid" required>
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
                    <div class="error">${error}</div>
                </c:if>
            </div>

            <!-- Chú thích / quy định nghỉ phép -->
            <div class="info-box">
                <h2>Quy định nghỉ phép</h2>
                <ul>
                    <li>Mỗi nhân viên được nghỉ phép tối đa 12 ngày/năm.</li>
                    <li>Đơn xin nghỉ phải gửi trước ít nhất 2 ngày làm việc.</li>
                    <li>Nếu nghỉ bệnh, cần kèm giấy chứng nhận y tế.</li>
                    <li>Người quản lý có quyền phê duyệt hoặc từ chối đơn.</li>
                    <li>Ngày lễ và cuối tuần không tính vào ngày nghỉ phép.</li>
                </ul>
                <p>Vui lòng điền đầy đủ thông tin để đơn được phê duyệt nhanh chóng.</p>
            </div>

        </div>

    </body>
</html>

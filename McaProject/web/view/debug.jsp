<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Debug - User Info & Roles</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f9f9f9;
            margin: 30px;
        }
        h2 {
            color: #2c3e50;
            margin-bottom: 10px;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            background: white;
            box-shadow: 0 0 5px rgba(0,0,0,0.1);
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        th {
            background-color: #3498db;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        .info {
            background: white;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            box-shadow: 0 0 5px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <h2>🛠 Debug - User & Role Information</h2>

    <div class="info">
        <h3>Thông tin User hiện tại:</h3>
        <c:choose>
            <c:when test="${not empty user}">
                <p><b>ID:</b> ${user.id}</p>
                <p><b>Tên hiển thị:</b> ${user.disname}</p>
                <p><b>Tài khoản:</b> ${user.account}</p>
                <p><b>Trạng thái:</b> 
                    <c:if test="${user.isActive}">Hoạt động</c:if>
                    <c:if test="${not user.isActive}">Bị khóa</c:if>
                </p>
            </c:when>
            <c:otherwise>
                <p style="color:red;">⚠ Không có user nào trong session!</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="info">
        <h3>Roles và Features của user:</h3>
        <c:choose>
            <c:when test="${not empty user.roles}">
                <table>
                    <tr>
                        <th>Role</th>
                        <th>Feature URLs</th>
                    </tr>
                    <c:forEach var="r" items="${user.roles}">
                        <tr>
                            <td>${r.roleName}</td>
                            <td>
                                <c:forEach var="f" items="${r.features}">
                                    <div>${f.fUrl}</div>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <p>❌ User này chưa có role nào được gán.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="info">
        <h3>Log debug (console output):</h3>
        <p>Xem tại cửa sổ <b>Output / Console</b> của NetBeans hoặc Tomcat log.</p>
    </div>
</body>
</html>

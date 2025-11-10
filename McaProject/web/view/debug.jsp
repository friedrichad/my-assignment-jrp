<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .debug-container {
        max-width: 900px;
        margin: 0 auto;
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 3px 10px rgba(0,0,0,0.08);
        padding: 30px 40px;
    }

    .debug-container h2 {
        color: #2563eb;
        font-size: 1.6em;
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        gap: 8px;
        font-weight: 600;
    }

    .debug-section {
        background: #f9fafb;
        border-radius: 8px;
        padding: 20px;
        margin-bottom: 25px;
        box-shadow: inset 0 0 3px rgba(0,0,0,0.05);
    }

    .debug-section h3 {
        color: #1e3a8a;
        margin-bottom: 12px;
        border-left: 4px solid #2563eb;
        padding-left: 10px;
    }

    .debug-section p {
        margin: 6px 0;
    }

    table {
        border-collapse: collapse;
        width: 100%;
        background: white;
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 2px 5px rgba(0,0,0,0.05);
    }

    th, td {
        padding: 10px 12px;
        border-bottom: 1px solid #e5e7eb;
        text-align: left;
    }

    th {
        background-color: #2563eb;
        color: white;
        text-transform: uppercase;
        font-size: 0.85em;
        letter-spacing: 0.5px;
    }

    tr:nth-child(even) {
        background-color: #f9fafb;
    }

    .alert {
        color: #dc2626;
        background: #fee2e2;
        padding: 10px 14px;
        border-radius: 8px;
        font-weight: 500;
    }

    .note {
        color: #6b7280;
        font-style: italic;
    }
</style>

<div class="debug-container">
    <h2>🛠 Debug - User & Role Information</h2>

    <div class="debug-section">
        <h3>Thông tin User hiện tại</h3>
        <c:choose>
            <c:when test="${not empty user}">
                <p><b>ID:</b> ${user.id}</p>
                <p><b>Tên hiển thị:</b> ${user.disname}</p>
                <p><b>Tài khoản:</b> ${user.account}</p>
                <p><b>Trạng thái:</b>
                    <c:choose>
                        <c:when test="${user.active}">
                            Hoạt động ✅
                        </c:when>
                        <c:otherwise>
                            Bị khóa ❌
                        </c:otherwise>
                    </c:choose>
                </p>
            </c:when>
            <c:otherwise>
                <p class="alert">⚠ Không có user nào trong session!</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="debug-section">
        <h3>Roles và Features của user</h3>
        <c:choose>
            <c:when test="${not empty user.roles}">
                <table>
                    <thead>
                        <tr>
                            <th>Role</th>
                            <th>Feature URLs</th>
                        </tr>
                    </thead>
                    <tbody>
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
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="alert">❌ User này chưa có role nào được gán.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="debug-section">
        <h3>Log debug (console output)</h3>
        <p class="note">Xem tại cửa sổ <b>Output / Console</b> của NetBeans hoặc file log Tomcat.</p>
    </div>
</div>

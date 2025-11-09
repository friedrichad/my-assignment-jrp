<%-- 
    Document   : profile
    Created on : Nov 9, 2025, 11:50:51 PM
    Author     : Hiro
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
.profile-container {
    max-width: 700px;
    margin: 40px auto;
    padding: 30px;
    background: rgba(255, 255, 255, 0.85);
    border-radius: 16px;
    backdrop-filter: blur(10px);
    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
    from {opacity: 0; transform: translateY(10px);}
    to {opacity: 1; transform: translateY(0);}
}

.profile-header {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 25px;
}

.profile-header img {
    width: 90px;
    height: 90px;
    border-radius: 50%;
    box-shadow: 0 2px 10px rgba(0,0,0,0.2);
    border: 3px solid #f3f4f6;
}

.profile-header h2 {
    font-size: 1.6em;
    color: #1f2937;
    margin: 0;
}

.profile-details {
    display: grid;
    grid-template-columns: 150px 1fr;
    row-gap: 14px;
    column-gap: 10px;
}

.profile-details label {
    font-weight: bold;
    color: #374151;
}

.profile-details span {
    color: #1e293b;
}

hr {
    border: none;
    border-top: 1px solid #e5e7eb;
    margin: 20px 0;
}
</style>

<div class="profile-container">
    <div class="profile-header">
        <img src="${pageContext.request.contextPath}/assets/img/avatar.png" alt="Avatar">
        <div>
            <h2>${employee.employeeName}</h2>
            <p style="color:#6b7280; margin:0;">${employee.division.divisionName}</p>
        </div>
    </div>

    <hr/>

    <div class="profile-details">
        <label>Mã nhân viên:</label> <span>${employee.id}</span>
        <label>Giới tính:</label> <span>${employee.gender ? "Nam" : "Nữ"}</span>
        <label>Ngày sinh:</label> <span>${employee.dob}</span>
        <label>Email:</label> <span>${employee.email}</span>
        <label>Phòng ban:</label> <span>${employee.division.divisionName}</span>
        <label>Người quản lý:</label> <span>${employee.supervisor.employeeName}</span>
    </div>
</div>
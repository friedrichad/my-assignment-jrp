<%-- 
    Document   : agenda.jsp
    Created on : Nov 7, 2025
    Author     : Hiro (Cập nhật bởi Grok)
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    /* PHÓNG GIÃN THEO MAIN LAYOUT */
    .agenda-wrapper {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        gap: 1.5rem;
    }

    .agenda-header {
        text-align: center;
        font-size: 1.75rem;
        font-weight: 700;
        color: #1f2937;
    }

    /* FILTER BAR */
    .filter-bar {
        display: flex;
        flex-wrap: wrap;
        gap: 0.75rem;
        align-items: center;
        background: white;
        padding: 1rem;
        border-radius: 8px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        border: 1px solid #e5e7eb;
    }
    .filter-bar input[type="text"],
    .filter-bar input[type="date"] {
        padding: 0.5rem 0.75rem;
        border: 1px solid #d1d5db;
        border-radius: 6px;
        font-size: 0.875rem;
        flex: 1;
        min-width: 150px;
    }
    .filter-bar button {
        padding: 0.5rem 1rem;
        background: #3b82f6;
        color: white;
        border: none;
        border-radius: 6px;
        font-weight: 600;
        cursor: pointer;
        white-space: nowrap;
    }
    .filter-bar button:hover {
        background: #2563eb;
    }
    .filter-bar a {
        color: #dc2626;
        text-decoration: none;
        font-weight: 500;
    }
    .filter-bar a:hover {
        text-decoration: underline;
    }

    /* CARD CONTAINER */
    .card {
        background: white;
        border-radius: 8px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        border: 1px solid #e5e7eb;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        flex: 1;
    }
    .card-header {
        background: #3b82f6;
        color: white;
        padding: 1rem;
        text-align: center;
        font-weight: 600;
        font-size: 1.1rem;
    }
    .card-nav {
        display: flex;
        justify-content: space-between;
        padding: 0.75rem 1.5rem;
        background: #f9fafb;
        font-size: 0.875rem;
    }
    .card-nav a {
        color: #3b82f6;
        text-decoration: none;
        font-weight: 500;
    }
    .card-nav a:hover {
        text-decoration: underline;
    }

    /* TABLE SCROLL */
    .table-container {
        flex: 1;
        overflow: auto;
        -webkit-overflow-scrolling: touch;
    }
    .table-container::-webkit-scrollbar {
        width: 6px;
        height: 6px;
    }
    .table-container::-webkit-scrollbar-track {
        background: #f3f4f6;
        border-radius: 10px;
    }
    .table-container::-webkit-scrollbar-thumb {
        background: #9ca3af;
        border-radius: 10px;
    }
    .table-container::-webkit-scrollbar-thumb:hover {
        background: #6b7280;
    }

    /* CALENDAR TABLE */
    .calendar-table {
        width: 100%;
        border-collapse: collapse;
        font-size: 0.875rem;
        min-width: 700px;
    }
    .calendar-table th {
        background: #f3f4f6;
        padding: 0.75rem;
        text-align: center;
        font-weight: 600;
        color: #374151;
    }
    .calendar-table td {
        height: 90px;
        vertical-align: top;
        padding: 0.5rem;
        border: 1px solid #e5e7eb;
        position: relative;
    }
    .calendar-table .other-month {
        color: #9ca3af;
        background: #f9fafb;
    }
    .calendar-table .day-number {
        font-weight: bold;
        color: #1f2937;
        margin-bottom: 0.25rem;
    }
    .leave-list {
        max-height: 60px;
        overflow-y: auto;
        display: flex;
        flex-direction: column;
        gap: 0.125rem;
    }
    .leave-item {
        font-size: 0.75rem;
        padding: 0.25rem 0.375rem;
        border-left: 3px solid;
        border-radius: 4px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .leave-approved {
        background: #ecfdf5;
        border-left-color: #10b981;
        color: #065f46;
    }
    .leave-pending {
        background: #fffbeb;
        border-left-color: #f59e0b;
        color: #92400e;
    }
    .leave-rejected {
        background: #fef2f2;
        border-left-color: #ef4444;
        color: #991b1b;
    }

    /* LIST TABLE */
    .list-table {
        width: 100%;
        border-collapse: collapse;
        min-width: 600px;
    }
    .list-table th {
        background: #f3f4f6;
        padding: 0.875rem 1rem;
        text-align: left;
        font-weight: 600;
        color: #374151;
        border-bottom: 1px solid #e5e7eb;
    }
    .list-table td {
        padding: 0.75rem 1rem;
        border-bottom: 1px solid #e5e7eb;
    }
    .list-table tr:hover {
        background: #f9fafb;
    }
    .status-badge {
        display: inline-block;
        padding: 0.25rem 0.5rem;
        font-size: 0.75rem;
        font-weight: 600;
        border-radius: 9999px;
    }
    .status-approved {
        background: #d1fae5;
        color: #065f46;
    }
    .status-pending {
        background: #fef3c7;
        color: #92400e;
    }
    .status-rejected {
        background: #fee2e2;
        color: #991b1b;
    }

    /* INFO & PAGINATION */
    .info-text {
        text-align: center;
        color: #6b7280;
        font-size: 0.875rem;
        padding: 1rem;
        background: #f9fafb;
    }
    .pagination {
        display: flex;
        justify-content: center;
        gap: 0.5rem;
        padding: 1rem;
        background: #f9fafb;
        border-top: 1px solid #e5e7eb;
    }
    .pagination a, .pagination span {
        padding: 0.5rem 0.75rem;
        border: 1px solid #d1d5db;
        border-radius: 6px;
        text-decoration: none;
        color: #374151;
        min-width: 36px;
        text-align: center;
        font-size: 0.875rem;
    }
    .pagination a:hover {
        background: #3b82f6;
        color: white;
        border-color: #3b82f6;
    }
    .pagination .current {
        background: #3b82f6;
        color: white;
        font-weight: bold;
    }

    /* RESPONSIVE */
    @media (max-width: 768px) {
        .filter-bar {
            flex-direction: column;
            align-items: stretch;
        }
        .filter-bar input[type="text"], .filter-bar input[type="date"] {
            min-width: 100%;
        }
        .calendar-table td {
            height: 70px;
            font-size: 0.75rem;
        }
        .leave-item {
            font-size: 0.7rem;
        }
    }
</style>

<div class="agenda-wrapper">
    <h1 class="agenda-header">Leave Agenda</h1>

    <!-- FILTER BAR -->
    <form method="get" action="agenda" class="filter-bar">
        <c:if test="${isManager}">
            <input type="text" name="search" placeholder="Search by name" value="${search}" />
        </c:if>
        <input type="date" name="from" value="${from}" required />
        <input type="date" name="to" value="${to}" required />
        <button type="submit">Filter</button>
        <a href="agenda">Reset</a>
    </form>

    <!-- CALENDAR CARD -->
    <div class="card">
        <div class="card-header">
            <fmt:formatDate value="${firstDay}" pattern="MMMM yyyy" />
        </div>
        <div class="card-nav">
            <a href="?search=${search}&from=${prevMonth}&to=${prevMonthEnd}">Previous</a>
            <a href="?search=${search}&from=${nextMonth}&to=${nextMonthEnd}">Next</a>
        </div>
        <div class="table-container">
            <table class="calendar-table">
                <thead>
                    <tr>
                        <th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="week" items="${calendarWeeks}">
                        <tr>
                            <c:forEach var="day" items="${week}">
                                <td class="${day.inMonth ? '' : 'other-month'}">
                                    <c:if test="${day.date != null}">
                                        <div class="day-number">${day.dayOfMonth}</div>
                                        <div class="leave-list">
                                            <c:forEach var="leave" items="${day.leaves}">
                                                <div class="leave-item leave-${leave.status.toLowerCase()}">
                                                    <c:if test="${isManager}">
                                                        <strong>${leave.employee.employeeName}:</strong>
                                                    </c:if>
                                                    ${leave.leaveTypeName}
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:if>
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- LIST CARD -->
    <div class="card">
        <div class="info-text">
            <c:choose>
                <c:when test="${totalRecords == 0}">
                    No leave requests found.
                </c:when>
                <c:otherwise>
                    Showing page <strong>${page}</strong> of <strong>${totalPages}</strong>
                    — <strong>${totalRecords}</strong> request<c:if test="${totalRecords != 1}">s</c:if> found.
                </c:otherwise>
            </c:choose>
        </div>

        <div class="table-container">
            <table class="list-table">
                <thead>
                    <tr>
                        <c:if test="${isManager}">
                            <th>Employee ID</th>
                            <th>Employee Name</th>
                            </c:if>
                        <th>Type</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Days</th>
                        <th>Status</th>
                        <th>Reason</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="leave" items="${leaves}">
                        <tr>
                            <c:if test="${isManager}">
                                <td>${leave.employee.id}</td>
                                <td>${leave.employee.employeeName}</td>
                            </c:if>
                            <td>${leave.leaveTypeName}</td>
                            <td><fmt:formatDate value="${leave.startDate}" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatDate value="${leave.endDate}" pattern="yyyy-MM-dd"/></td>
                            <td>${leave.numDays}</td>
                            <td><span class="status-badge status-${leave.status.toLowerCase()}">${leave.status}</span></td>
                            <td>${leave.reason}</td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty leaves}">
                        <tr>
                            <td colspan="${isManager ? 8 : 6}" style="text-align:center; color:#9ca3af; padding:2rem;">
                                No leave requests match your filters.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:choose>
                    <c:when test="${page > 1}">
                        <a href="?search=${search}&from=${from}&to=${to}&page=1">First</a>
                        <a href="?search=${search}&from=${from}&to=${to}&page=${page-1}">Previous</a>
                    </c:when>
                    <c:otherwise>
                        <span style="color:#9ca3af;">First</span>
                        <span style="color:#9ca3af;">Previous</span>
                    </c:otherwise>
                </c:choose>

                <c:forEach begin="${page-2 > 0 ? page-2 : 1}" 
                           end="${page+2 <= totalPages ? page+2 : totalPages}" var="p">
                    <c:choose>
                        <c:when test="${p == page}">
                            <span class="current">${p}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="?search=${search}&from=${from}&to=${to}&page=${p}">${p}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:choose>
                    <c:when test="${page < totalPages}">
                        <a href="?search=${search}&from=${from}&to=${to}&page=${page+1}">Next</a>
                        <a href="?search=${search}&from=${from}&to=${to}&page=${totalPages}">Last</a>
                    </c:when>
                    <c:otherwise>
                        <span style="color:#9ca3af;">Next</span>
                        <span style="color:#9ca3af;">Last</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </div>
</div>
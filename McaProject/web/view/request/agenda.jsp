<%-- 
    Document   : agenda
    Created on : Nov 7, 2025, 11:11:32 AM
    Author     : Hiro
--%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.LeaveRequest"%>
<%
    ArrayList<LeaveRequest> leaves = (ArrayList<LeaveRequest>) request.getAttribute("leaves");
    LocalDate today = LocalDate.now();
    LocalDate startOfMonth = today.withDayOfMonth(1);
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>L?ch làm vi?c c?a tôi</title>
    <link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.js"></script>

    <style>
        body { font-family: "Segoe UI", sans-serif; background: #f4f6f8; margin: 0; }
        h2 { text-align: center; padding: 20px; color: #333; }
        #calendar { max-width: 900px; margin: 40px auto; background: white; padding: 20px; border-radius: 16px;
                    box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
    </style>
</head>
<body>
    <h2>L?ch làm vi?c c?a tôi</h2>
    <div id="calendar"></div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var events = [];

            <%-- Ngh? phép (??) --%>
            <% for (LeaveRequest lr : leaves) { %>
                events.push({
                    title: 'Ngh? phép',
                    start: '<%= lr.getStartDate().toLocalDate() %>',
                    end: '<%= lr.getEndDate().toLocalDate().plusDays(1) %>',
                    color: '#e74c3c'
                });
            <% } %>

            <%-- ?i làm (xanh) --%>
            <% 
                LocalDate todayLoop = startOfMonth;
                while (!todayLoop.isAfter(today)) {
                    boolean isLeave = false;
                    for (LeaveRequest lr : leaves) {
                        LocalDate start = lr.getStartDate().toLocalDate();
                        LocalDate end = lr.getEndDate().toLocalDate();
                        if (!todayLoop.isBefore(start) && !todayLoop.isAfter(end)) {
                            isLeave = true; break;
                        }
                    }
                    if (!isLeave) {
            %>
                events.push({
                    title: '?i làm',
                    start: '<%= todayLoop %>',
                    end: '<%= todayLoop.plusDays(1) %>',
                    color: '#2ecc71'
                });
            <% }
                todayLoop = todayLoop.plusDays(1);
            } %>

            var calendarEl = document.getElementById('calendar');
            var calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',
                locale: 'vi',
                height: 'auto',
                headerToolbar: { left: 'prev,next today', center: 'title', right: 'dayGridMonth,listMonth' },
                events: events
            });
            calendar.render();
        });
    </script>
</body>
</html>
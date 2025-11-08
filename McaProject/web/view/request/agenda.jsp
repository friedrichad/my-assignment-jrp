<%-- 
    Document   : agenda
    Created on : Nov 7, 2025, 11:11:32 AM
    Author     : Hiro
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lịch làm việc của tôi</title>

    <!-- FullCalendar CSS -->
    <link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.css" rel="stylesheet">

    <!-- FullCalendar JS -->
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.js"></script>

    <style>
        body {
            font-family: "Segoe UI", sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }

        h2 {
            text-align: center;
            padding: 20px;
            color: #333;
        }

        #calendar {
            max-width: 900px;
            margin: 40px auto;
            background: white;
            padding: 20px;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }

        .fc-daygrid-event {
            border-radius: 8px;
            font-size: 0.9em;
            text-align: center;
        }
    </style>
</head>
<body>
    <h2>Lịch làm việc của tôi</h2>
    <div id="calendar"></div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const calendarEl = document.getElementById('calendar');

            const calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',
                locale: 'vi', // ngôn ngữ tiếng Việt
                height: 'auto',
                headerToolbar: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'dayGridMonth,listMonth'
                },
                events: {
                    url: '<%= request.getContextPath() %>/request/agenda',
                    failure: function() {
                        alert('Không thể tải dữ liệu lịch!');
                    }
                },
                eventDidMount: function(info) {
                    // Tooltip gợi ý nhanh
                    const tooltip = document.createElement('div');
                    tooltip.innerText = info.event.title;
                    tooltip.style.position = 'absolute';
                    tooltip.style.background = 'rgba(0,0,0,0.7)';
                    tooltip.style.color = 'white';
                    tooltip.style.padding = '5px 8px';
                    tooltip.style.borderRadius = '6px';
                    tooltip.style.fontSize = '12px';
                    tooltip.style.display = 'none';
                    document.body.appendChild(tooltip);

                    info.el.addEventListener('mouseenter', function(e) {
                        tooltip.style.left = e.pageX + 10 + 'px';
                        tooltip.style.top = e.pageY + 'px';
                        tooltip.style.display = 'block';
                    });

                    info.el.addEventListener('mouseleave', function() {
                        tooltip.style.display = 'none';
                    });
                }
            });

            calendar.render();
        });
    </script>
</body>
</html>
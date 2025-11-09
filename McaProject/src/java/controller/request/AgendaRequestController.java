/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.request;

import controller.auth.BaseAuthorizationController;
import dal.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import model.LeaveRequest;
import model.Employee;
import model.self.User;

/**
 *
 * @author Hiro
 */
@WebServlet("/request/agenda")
public class AgendaRequestController extends BaseAuthorizationController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int PAGE_SIZE = 10;

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        LeaveRequestDBContext leaveDB = new LeaveRequestDBContext();
        RoleDBContext roleDB = new RoleDBContext();
        AgendaDBContext agendaDB = new AgendaDBContext();
        UserDBContext userDB = new UserDBContext();

        // === 1. Lấy eid từ uid ===
        int eid = leaveDB.getEidByUid(user.getId());
        if (eid == -1) {
            resp.sendError(403, "Employee not found.");
            return;
        }

        // === 2. Kiểm tra quyền ===
        user.setRoles(roleDB.getByUserId(user.getId()));
        boolean isManager = user.hasRole("Manager") || user.hasRole("Admin");

        // === 3. Lấy tham số ===
        String search = req.getParameter("search");
        String fromStr = req.getParameter("from");
        String toStr = req.getParameter("to");
        String pageStr = req.getParameter("page");

        LocalDate from = parseDate(fromStr, LocalDate.now().withDayOfMonth(1));
        LocalDate to = parseDate(toStr, from.withDayOfMonth(from.lengthOfMonth()));
        int page = parseInt(pageStr, 1);

        // === 4. LẤY DANH SÁCH NHÂN VIÊN (AN TOÀN, KHÔNG GỌI DB THỪA) ===
        List<Employee> subordinates = new ArrayList<>();

        if (isManager) {
            // Lấy cấp dưới (đã có tên)
            subordinates = userDB.getAllSubordinates(eid);

            // Tìm bản thân trong danh sách
            Employee self = subordinates.stream()
                    .filter(emp -> emp.getId() == eid)
                    .findFirst()
                    .orElse(null);

            if (self == null) {
                self = new Employee();
                self.setId(eid);
                self.setEmployeeName("Me");
                subordinates.add(0, self);
            }

            // TÌM KIẾM: chỉ lọc tên (đã có trong Employee)
            if (search != null && !search.trim().isEmpty()) {
                String keyword = search.trim().toLowerCase();
                subordinates.removeIf(emp -> {
                    String name = emp.getEmployeeName();
                    return name == null || !name.toLowerCase().contains(keyword);
                });
            }
        } else {
            // Nhân viên thường: chỉ bản thân
            Employee self = new Employee();
            self.setId(eid);
            self.setEmployeeName("Me");
            subordinates.add(self);
        }

        List<Integer> eids = subordinates.stream()
                .map(Employee::getId)
                .collect(Collectors.toList());

        // === 5. PHÂN TRANG ===
        int totalRecords = agendaDB.countLeavesByEmployeesAndDateRange(eids, from, to);
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
        if (page < 1) {
            page = 1;
        }
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int offset = (page - 1) * PAGE_SIZE;
        List<LeaveRequest> leaves = agendaDB.getLeavesByEmployeesAndDateRangePaged(eids, from, to, offset, PAGE_SIZE);

        // === 6. XÂY DỰNG LỊCH THÁNG (KHÔNG toLocalDate()) ===
        YearMonth yearMonth = YearMonth.from(from);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        LocalDate lastOfMonth = yearMonth.atEndOfMonth();

        // Prev / Next
        LocalDate prevMonth = yearMonth.minusMonths(1).atDay(1);
        LocalDate prevMonthEnd = yearMonth.minusMonths(1).atEndOfMonth();
        LocalDate nextMonth = yearMonth.plusMonths(1).atDay(1);
        LocalDate nextMonthEnd = yearMonth.plusMonths(1).atEndOfMonth();

        // Build calendar grid
        List<List<Map<String, Object>>> calendarWeeks = new ArrayList<>();
        LocalDate day = firstOfMonth.minusDays(firstOfMonth.getDayOfWeek().getValue() % 7);

        while (day.isBefore(lastOfMonth.plusDays(7))) {
            List<Map<String, Object>> week = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                Map<String, Object> cell = new HashMap<>();
                boolean inMonth = !day.isBefore(firstOfMonth) && !day.isAfter(lastOfMonth);
                cell.put("date", inMonth ? java.sql.Date.valueOf(day) : null);
                cell.put("dayOfMonth", day.getDayOfMonth());
                cell.put("inMonth", inMonth);
                cell.put("leaves", new ArrayList<LeaveRequest>());
                week.add(cell);
                day = day.plusDays(1);
            }
            calendarWeeks.add(week);
        }

        // Fill leaves into calendar (Date → LocalDate an toàn)
        for (LeaveRequest leave : leaves) {
            Date startDate = leave.getStartDate();
            Date endDate = leave.getEndDate();
            if (startDate == null || endDate == null) {
                continue;
            }

            LocalDate start = new Date(startDate.getTime()).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = new Date(endDate.getTime()).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                if (d.getMonth() == yearMonth.getMonth() && d.getYear() == yearMonth.getYear()) {
                    int weekIndex = (d.getDayOfWeek().getValue() + 6) / 7;
                    int dayIndex = d.getDayOfWeek().getValue() % 7;
                    try {
                        List<LeaveRequest> dayLeaves = (List<LeaveRequest>) calendarWeeks.get(weekIndex).get(dayIndex).get("leaves");
                        dayLeaves.add(leave);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        // === 7. TRUYỀN DỮ LIỆU ===
        req.setAttribute("leaves", leaves);
        req.setAttribute("isManager", isManager);
        req.setAttribute("subordinates", subordinates);
        req.setAttribute("search", search);
        req.setAttribute("from", from.format(DATE_FORMAT));
        req.setAttribute("to", to.format(DATE_FORMAT));
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalRecords", totalRecords);

        // Calendar
        req.setAttribute("firstDay", java.sql.Date.valueOf(firstOfMonth));
        req.setAttribute("prevMonth", prevMonth.format(DATE_FORMAT));
        req.setAttribute("prevMonthEnd", prevMonthEnd.format(DATE_FORMAT));
        req.setAttribute("nextMonth", nextMonth.format(DATE_FORMAT));
        req.setAttribute("nextMonthEnd", nextMonthEnd.format(DATE_FORMAT));
        req.setAttribute("calendarWeeks", calendarWeeks);

        req.setAttribute("pageTitle", "Leave Agenda");
        req.setAttribute("contentPage", "/view/request/agenda.jsp");
        req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        resp.sendError(405, "POST method not supported.");
    }

    // === HELPER METHODS ===
    private LocalDate parseDate(String s, LocalDate def) {
        if (s == null || s.trim().isEmpty()) {
            return def;
        }
        try {
            return LocalDate.parse(s.trim(), DATE_FORMAT);
        } catch (Exception e) {
            return def;
        }
    }

    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }
}

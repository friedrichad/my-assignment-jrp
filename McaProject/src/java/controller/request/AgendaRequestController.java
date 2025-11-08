/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.request;

import controller.auth.BaseAuthorizationController;
import dal.LeaveRequestDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import model.LeaveRequest;
import model.self.User;

/**
 *
 * @author Hiro
 */
@WebServlet("/request/agenda")
public class AgendaRequestController extends BaseAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        // Không dùng POST
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");
        LeaveRequestDBContext db = new LeaveRequestDBContext();

        int userId = user.getId();
        ArrayList<LeaveRequest> leaves = db.getRequestsByUser(userId); // ✅ dùng hàm này

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        StringBuilder json = new StringBuilder();
        json.append("[");

        boolean first = true;

        // 🔴 Nghỉ phép (Approved)
        for (LeaveRequest lr : leaves) {
            if ("approved".equalsIgnoreCase(lr.getStatus())) {
                LocalDate start = lr.getStartDate().toLocalDate();
                LocalDate end = lr.getEndDate().toLocalDate();

                if (!first) json.append(",");
                json.append("{")
                    .append("\"title\":\"Nghỉ phép\",")
                    .append("\"start\":\"").append(start).append("\",")
                    .append("\"end\":\"").append(end.plusDays(1)).append("\",")
                    .append("\"color\":\"#e74c3c\"")
                    .append("}");
                first = false;
            }
        }

        // 🟩 Đi làm (chỉ từ đầu tháng đến hôm nay)
        for (LocalDate d = startOfMonth; !d.isAfter(today); d = d.plusDays(1)) {
            boolean isLeave = false;

            for (LeaveRequest lr : leaves) {
                if ("approved".equalsIgnoreCase(lr.getStatus())) {
                    LocalDate start = lr.getStartDate().toLocalDate();
                    LocalDate end = lr.getEndDate().toLocalDate();
                    if (!d.isBefore(start) && !d.isAfter(end)) {
                        isLeave = true;
                        break;
                    }
                }
            }

            if (!isLeave) {
                if (!first) json.append(",");
                json.append("{")
                    .append("\"title\":\"Đi làm\",")
                    .append("\"start\":\"").append(d).append("\",")
                    .append("\"end\":\"").append(d.plusDays(1)).append("\",")
                    .append("\"color\":\"#2ecc71\"")
                    .append("}");
                first = false;
            }
        }

        json.append("]");

        try (PrintWriter out = resp.getWriter()) {
            out.print(json.toString());
        }
    }
}